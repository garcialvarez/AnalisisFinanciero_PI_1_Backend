package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.DTO.ClasificadorPresupuestalDTO;
import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoClasificadorDTO;
import com.udea.AnalisisFinanciero_back.DTO.response.ClasificadorConDetallesDTO;
import com.udea.AnalisisFinanciero_back.entity.ClasificadorPresupuestal;
import com.udea.AnalisisFinanciero_back.exceptions.ResourceNotFoundException;
import com.udea.AnalisisFinanciero_back.mapper.ClasificadorPresupuestalMapper;
import com.udea.AnalisisFinanciero_back.mapper.DetallePonderadoMapper;
import com.udea.AnalisisFinanciero_back.repository.ClasificadorPresupuestalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClasificadorPresupuestalService {

    private final ClasificadorPresupuestalRepository clasificadorRepository;
    private final ClasificadorPresupuestalMapper clasificadorMapper;
    private final DetallePonderadoMapper detallePonderadoMapper;

    /**
     * Busca un clasificador por código y retorna datos genéricos (sin detalles ponderados)
     */
    public ClasificadorPresupuestalDTO buscarPorCodigo(String codigo) {
        ClasificadorPresupuestal clasificador = clasificadorRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Clasificador no encontrado con código: " + codigo));
        
        return clasificadorMapper.toDTO(clasificador);
    }

    /**
     * Busca un clasificador por código incluyendo sus detalles ponderados
     */
    public ClasificadorConDetallesDTO buscarPorCodigoConDetalles(String codigo) {
        ClasificadorPresupuestal clasificador = clasificadorRepository.findByCodigoWithDetalles(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Clasificador no encontrado con código: " + codigo));
        
        return convertirADTOConDetalles(clasificador);
    }

    /**
     * Obtiene todos los clasificadores con sus detalles ponderados de forma paginada
     */
    public Page<ClasificadorConDetallesDTO> obtenerTodosConDetalles(Pageable pageable) {
        Page<ClasificadorPresupuestal> clasificadores = clasificadorRepository.findAllWithDetalles(pageable);
        
        List<ClasificadorConDetallesDTO> clasificadoresDTO = clasificadores.getContent()
                .stream()
                .map(this::convertirADTOConDetalles)
                .collect(Collectors.toList());
        
        return new PageImpl<>(clasificadoresDTO, pageable, clasificadores.getTotalElements());
    }

    /**
     * Convierte entidad a DTO con detalles ponderados usando mappers
     */
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

}