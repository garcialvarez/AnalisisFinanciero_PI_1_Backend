package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.DTO.ClasificadorPresupuestalDTO;
import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoClasificadorDTO;
import com.udea.AnalisisFinanciero_back.DTO.response.ClasificadorConDetallesDTO;
import com.udea.AnalisisFinanciero_back.entity.ClasificadorPresupuestal;
import com.udea.AnalisisFinanciero_back.exceptions.ResourceNotFoundException;
import com.udea.AnalisisFinanciero_back.exceptions.ValidationException;
import com.udea.AnalisisFinanciero_back.mapper.ClasificadorPresupuestalMapper;
import com.udea.AnalisisFinanciero_back.mapper.DetallePonderadoMapper;
import com.udea.AnalisisFinanciero_back.repository.ClasificadorPresupuestalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ClasificadorPresupuestalService {

    private final ClasificadorPresupuestalRepository clasificadorRepository;
    private final ClasificadorPresupuestalMapper clasificadorMapper;
    private final DetallePonderadoMapper detallePonderadoMapper;

    //Busca un clasificador por código y retorna datos genéricos (sin detalles ponderados)
    public ClasificadorPresupuestalDTO buscarPorCodigo(String codigo) {
        log.info("Buscando clasificador presupuestal por código: {}", codigo);
        validateCodigoFormat(codigo);
        
        ClasificadorPresupuestal clasificador = clasificadorRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Clasificador no encontrado con código: " + codigo));
        
        log.info("Clasificador presupuestal encontrado: {}", clasificador.getNombreClasificador());
        return clasificadorMapper.toDTO(clasificador);
    }

    //Busca un clasificador por código incluyendo sus detalles ponderados
    public ClasificadorConDetallesDTO buscarPorCodigoConDetalles(String codigo) {
        log.info("Buscando clasificador presupuestal con detalles por código: {}", codigo);
        validateCodigoFormat(codigo);
        
        ClasificadorPresupuestal clasificador = clasificadorRepository.findByCodigoWithDetalles(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Clasificador no encontrado con código: " + codigo));
        
        log.info("Clasificador presupuestal con detalles encontrado: {} ({} detalles)", 
                clasificador.getNombreClasificador(), 
                clasificador.getDetallesPonderados() != null ? clasificador.getDetallesPonderados().size() : 0);
        return convertirADTOConDetalles(clasificador);
    }

    //Obtiene todos los clasificadores con sus detalles ponderados de forma paginada
    public Page<ClasificadorConDetallesDTO> obtenerTodosConDetalles(Pageable pageable) {
        log.info("Obteniendo todos los clasificadores presupuestales con detalles - Página: {}, Tamaño: {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        
        Page<ClasificadorPresupuestal> clasificadores = clasificadorRepository.findAllWithDetalles(pageable);
        
        List<ClasificadorConDetallesDTO> clasificadoresDTO = clasificadores.getContent()
                .stream()
                .map(this::convertirADTOConDetalles)
                .collect(Collectors.toList());
        
        log.info("Clasificadores presupuestales obtenidos: {} de {} total", 
                clasificadoresDTO.size(), clasificadores.getTotalElements());
        
        return new PageImpl<>(clasificadoresDTO, pageable, clasificadores.getTotalElements());
    }

    //Obtiene todos los clasificadores con sus detalles ponderados con parámetros de paginación
    public Page<ClasificadorConDetallesDTO> obtenerTodosConDetalles(
            int page, int size, String sortBy, String sortDir) {
        
        log.info("Obteniendo clasificadores presupuestales - Página: {}, Tamaño: {}, Ordenar por: {}, Dirección: {}", 
                page, size, sortBy, sortDir);
        
        validatePaginationParameters(page, size, sortDir);
        
        // Crear el objeto Sort
        Sort sort = createSort(sortBy, sortDir);
        
        // Crear el Pageable
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return obtenerTodosConDetalles(pageable);
    }

    //Convierte entidad a DTO con detalles ponderados usando mappers
    private ClasificadorConDetallesDTO convertirADTOConDetalles(ClasificadorPresupuestal clasificador) {
        ClasificadorConDetallesDTO dto = clasificadorMapper.toConDetallesDTO(clasificador);
        
        // Convertir detalles ponderados si existen usando el mapper especializado
        if (clasificador.getDetallesPonderados() != null && !clasificador.getDetallesPonderados().isEmpty()) {
            List<DetallePonderadoClasificadorDTO> detalles = clasificador.getDetallesPonderados()
                    .stream()
                    .map(detallePonderadoMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setDetallesPonderados(detalles);
        }
        
        return dto;
    }

    //Valida el formato del código del clasificador presupuestal
    private void validateCodigoFormat(String codigo) {
        if (!StringUtils.hasText(codigo)) {
            throw new ValidationException(Map.of("codigo", "El código del clasificador presupuestal no puede estar vacío"));
        }
        
        if (codigo.length() < 2 || codigo.length() > 20) {
            throw new ValidationException(Map.of("codigo", "El código del clasificador presupuestal debe tener entre 2 y 20 caracteres"));
        }
    }

    //Valida los parámetros de paginación
    private void validatePaginationParameters(int page, int size, String sortDir) {
        if (page < 0) {
            throw new ValidationException(Map.of("page", "El número de página debe ser mayor o igual a 0"));
        }
        
        if (size <= 0) {
            throw new ValidationException(Map.of("size", "El tamaño de página debe ser mayor a 0"));
        }
        
        if (size > 100) {
            throw new ValidationException(Map.of("size", "El tamaño de página no puede ser mayor a 100"));
        }
        
        if (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")) {
            throw new ValidationException(Map.of("sortDir", "La dirección de ordenamiento debe ser 'asc' o 'desc'"));
        }
    }

    //Crea el objeto Sort basado en el campo y dirección
    private Sort createSort(String sortBy, String sortDir) {
        // Lista de campos válidos para ordenar (seguridad)
        List<String> validSortFields = List.of("codigo", "nombre", "descripcion", "clasificadorId");
        
        if (!validSortFields.contains(sortBy)) {
            log.warn("Campo de ordenamiento no válido: {}. Usando 'codigo' por defecto", sortBy);
            sortBy = "codigo";
        }
        
        return sortDir.equalsIgnoreCase("desc") ? 
               Sort.by(sortBy).descending() : 
               Sort.by(sortBy).ascending();
    }

}