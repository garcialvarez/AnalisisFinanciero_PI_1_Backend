package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.DTO.CentroGestorDTO;
import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoCentroGestorDTO;
import com.udea.AnalisisFinanciero_back.DTO.response.CentroGestorConDetallesDTO;
import com.udea.AnalisisFinanciero_back.entity.CentroGestor;
import com.udea.AnalisisFinanciero_back.exceptions.ResourceNotFoundException;
import com.udea.AnalisisFinanciero_back.mapper.CentroGestorMapper;
import com.udea.AnalisisFinanciero_back.mapper.DetallePonderadoMapper;
import com.udea.AnalisisFinanciero_back.repository.CentroGestorRepository;
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
public class CentroGestorService {

    private final CentroGestorRepository centroGestorRepository;
    private final CentroGestorMapper centroGestorMapper;
    private final DetallePonderadoMapper detallePonderadoMapper;

    /**
     * Busca un centro gestor por código y retorna datos genéricos (sin detalles ponderados)
     */
    public CentroGestorDTO buscarPorCodigo(String codigo) {
        CentroGestor centroGestor = centroGestorRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Centro Gestor no encontrado con código: " + codigo));
        
        return centroGestorMapper.toDTO(centroGestor);
    }

    /**
     * Busca un centro gestor por código incluyendo sus detalles ponderados
     */
    public CentroGestorConDetallesDTO buscarPorCodigoConDetalles(String codigo) {
        CentroGestor centroGestor = centroGestorRepository.findByCodigoWithDetalles(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Centro Gestor no encontrado con código: " + codigo));
        
        return convertirADTOConDetalles(centroGestor);
    }

    /**
     * Obtiene todos los centros gestores con sus detalles ponderados de forma paginada
     */
    public Page<CentroGestorConDetallesDTO> obtenerTodosConDetalles(Pageable pageable) {
        Page<CentroGestor> centrosGestores = centroGestorRepository.findAllWithDetalles(pageable);
        
        List<CentroGestorConDetallesDTO> centrosGestoresDTO = centrosGestores.getContent()
                .stream()
                .map(this::convertirADTOConDetalles)
                .collect(Collectors.toList());
        
        return new PageImpl<>(centrosGestoresDTO, pageable, centrosGestores.getTotalElements());
    }

    /**
     * Convierte entidad a DTO con detalles ponderados usando mappers
     */
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

}