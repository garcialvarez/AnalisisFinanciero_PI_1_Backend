package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoCentroGestorDTO;
import com.udea.AnalisisFinanciero_back.entity.CentroGestor;
import com.udea.AnalisisFinanciero_back.entity.DetallePonderadoCentroGestor;
import com.udea.AnalisisFinanciero_back.exceptions.ResourceNotFoundException;
import com.udea.AnalisisFinanciero_back.mapper.DetallePonderadoMapper;
import com.udea.AnalisisFinanciero_back.repository.CentroGestorRepository;
import com.udea.AnalisisFinanciero_back.repository.DetallePonderadoCentroGestorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetallePonderadoCentroGestorService {
    private final DetallePonderadoCentroGestorRepository repository;
    private final CentroGestorRepository centroGestorRepository;
    private final DetallePonderadoMapper detallePonderadoMapper;

    @Transactional
    public DetallePonderadoCentroGestor guardarDetalle(DetallePonderadoCentroGestorDTO dto) {
        validarPorcentajes(dto);
        
        // Buscar el CentroGestor por ID
        CentroGestor centroGestor = centroGestorRepository.findById(dto.getCentroGestorId())
                .orElseThrow(() -> new ResourceNotFoundException("Centro Gestor no encontrado con ID: " + dto.getCentroGestorId()));
        
        // Mapear DTO -> Entidad
        DetallePonderadoCentroGestor detalle = new DetallePonderadoCentroGestor();
        detalle.setCentroGestor(centroGestor);
        detalle.setEnero(dto.getEnero());
        detalle.setFebrero(dto.getFebrero());
        detalle.setMarzo(dto.getMarzo());
        detalle.setAbril(dto.getAbril());
        detalle.setMayo(dto.getMayo());
        detalle.setJunio(dto.getJunio());
        detalle.setJulio(dto.getJulio());
        detalle.setAgosto(dto.getAgosto());
        detalle.setSeptiembre(dto.getSeptiembre());
        detalle.setOctubre(dto.getOctubre());
        detalle.setNoviembre(dto.getNoviembre());
        detalle.setDiciembre(dto.getDiciembre());
        detalle.setTotal(dto.getTotal());
        
        return repository.save(detalle);
    }

    private void validarPorcentajes(DetallePonderadoCentroGestorDTO dto) {
        BigDecimal suma = dto.getEnero()
                .add(dto.getFebrero())
                .add(dto.getMarzo())
                .add(dto.getAbril())
                .add(dto.getMayo())
                .add(dto.getJunio())
                .add(dto.getJulio())
                .add(dto.getAgosto())
                .add(dto.getSeptiembre())
                .add(dto.getOctubre())
                .add(dto.getNoviembre())
                .add(dto.getDiciembre());
        if (suma.compareTo(new BigDecimal("100.00")) != 0) {
            throw new IllegalArgumentException("La suma de los porcentajes de los meses debe ser igual a 100%");
        }
        if (dto.getTotal().compareTo(new BigDecimal("100.00")) != 0) {
            throw new IllegalArgumentException("El total debe ser igual a 100%");
        }
    }

    /**
     * Convierte detalle ponderado entidad a DTO usando mapper
     */
    public DetallePonderadoCentroGestorDTO convertirEntidadADTO(DetallePonderadoCentroGestor detalle) {
        return detallePonderadoMapper.toDTO(detalle);
    }

    // Otros métodos CRUD y de consulta
}
