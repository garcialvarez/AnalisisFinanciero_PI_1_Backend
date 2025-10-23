package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.DTO.CentroGestorDTO;
import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoCentroGestorDTO;
import com.udea.AnalisisFinanciero_back.DTO.response.CentroGestorConDetallesDTO;
import com.udea.AnalisisFinanciero_back.entity.CentroGestor;
import com.udea.AnalisisFinanciero_back.exceptions.ResourceNotFoundException;
import com.udea.AnalisisFinanciero_back.exceptions.ValidationException;
import com.udea.AnalisisFinanciero_back.mapper.CentroGestorMapper;
import com.udea.AnalisisFinanciero_back.mapper.DetallePonderadoMapper;
import com.udea.AnalisisFinanciero_back.repository.CentroGestorRepository;
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
public class CentroGestorService {

    private final CentroGestorRepository centroGestorRepository;
    private final CentroGestorMapper centroGestorMapper;
    private final DetallePonderadoMapper detallePonderadoMapper;

    //Busca un centro gestor por código y retorna datos genéricos (sin detalles ponderados)
    public CentroGestorDTO buscarPorCodigo(String codigo) {
        log.info("Buscando centro gestor por código: {}", codigo);
        validateCodigoFormat(codigo);
        
        CentroGestor centroGestor = centroGestorRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Centro Gestor no encontrado con código: " + codigo));
        
        log.info("Centro gestor encontrado: {}", centroGestor.getNombreCentroGestor());
        return centroGestorMapper.toDTO(centroGestor);
    }

    //Busca un centro gestor por código incluyendo sus detalles ponderados
    public CentroGestorConDetallesDTO buscarPorCodigoConDetalles(String codigo) {
        log.info("Buscando centro gestor con detalles por código: {}", codigo);
        validateCodigoFormat(codigo);
        
        CentroGestor centroGestor = centroGestorRepository.findByCodigoWithDetalles(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Centro Gestor no encontrado con código: " + codigo));
        
        log.info("Centro gestor con detalles encontrado: {} ({} detalles)", 
                centroGestor.getNombreCentroGestor(), 
                centroGestor.getDetallesPonderados() != null ? centroGestor.getDetallesPonderados().size() : 0);
        return convertirADTOConDetalles(centroGestor);
    }

    //Obtiene todos los centros gestores con sus detalles ponderados de forma paginada
    public Page<CentroGestorConDetallesDTO> obtenerTodosConDetalles(Pageable pageable) {
        log.info("Obteniendo todos los centros gestores con detalles - Página: {}, Tamaño: {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        
        Page<CentroGestor> centrosGestores = centroGestorRepository.findAllWithDetalles(pageable);
        
        List<CentroGestorConDetallesDTO> centrosGestoresDTO = centrosGestores.getContent()
                .stream()
                .map(this::convertirADTOConDetalles)
                .collect(Collectors.toList());
        
        log.info("Centros gestores obtenidos: {} de {} total", 
                centrosGestoresDTO.size(), centrosGestores.getTotalElements());
        
        return new PageImpl<>(centrosGestoresDTO, pageable, centrosGestores.getTotalElements());
    }

    //Obtiene todos los centros gestores con sus detalles ponderados con parámetros de paginación
    public Page<CentroGestorConDetallesDTO> obtenerTodosConDetalles(
            int page, int size, String sortBy, String sortDir) {
        
        log.info("Obteniendo centros gestores - Página: {}, Tamaño: {}, Ordenar por: {}, Dirección: {}", 
                page, size, sortBy, sortDir);
        
        validatePaginationParameters(page, size, sortDir);
        
        // Crear el objeto Sort
        Sort sort = createSort(sortBy, sortDir);
        
        // Crear el Pageable
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return obtenerTodosConDetalles(pageable);
    }

    //Convierte entidad a DTO con detalles ponderados usando mappers
    private CentroGestorConDetallesDTO convertirADTOConDetalles(CentroGestor centroGestor) {
        CentroGestorConDetallesDTO dto = centroGestorMapper.toConDetallesDTO(centroGestor);
        
        // Convertir detalles ponderados si existen usando el mapper especializado
        if (centroGestor.getDetallesPonderados() != null && !centroGestor.getDetallesPonderados().isEmpty()) {
            List<DetallePonderadoCentroGestorDTO> detalles = centroGestor.getDetallesPonderados()
                    .stream()
                    .map(detallePonderadoMapper::toDTO)
                    .collect(Collectors.toList());
            dto.setDetallesPonderados(detalles);
        }
        
        return dto;
    }

    //Valida el formato del código del centro gestor
    private void validateCodigoFormat(String codigo) {
        if (!StringUtils.hasText(codigo)) {
            throw new ValidationException(Map.of("codigo", "El código del centro gestor no puede estar vacío"));
        }
        
        if (codigo.length() < 2 || codigo.length() > 20) {
            throw new ValidationException(Map.of("codigo", "El código del centro gestor debe tener entre 2 y 20 caracteres"));
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
        List<String> validSortFields = List.of("codigo", "nombre", "descripcion", "centroGestorId");
        
        if (!validSortFields.contains(sortBy)) {
            log.warn("Campo de ordenamiento no válido: {}. Usando 'codigo' por defecto", sortBy);
            sortBy = "codigo";
        }
        
        return sortDir.equalsIgnoreCase("desc") ? 
               Sort.by(sortBy).descending() : 
               Sort.by(sortBy).ascending();
    }

}