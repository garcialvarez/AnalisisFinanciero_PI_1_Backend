package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoClasificadorDTO;
import com.udea.AnalisisFinanciero_back.entity.ClasificadorPresupuestal;
import com.udea.AnalisisFinanciero_back.entity.DetallePonderadoClasificador;
import com.udea.AnalisisFinanciero_back.exceptions.ResourceNotFoundException;
import com.udea.AnalisisFinanciero_back.mapper.DetallePonderadoMapper;
import com.udea.AnalisisFinanciero_back.repository.ClasificadorPresupuestalRepository;
import com.udea.AnalisisFinanciero_back.repository.DetallePonderadoClasificadorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetallePonderadoClasificadorService {
    private final DetallePonderadoClasificadorRepository repository;
    private final ClasificadorPresupuestalRepository clasificadorRepository;
    private final DetallePonderadoMapper detallePonderadoMapper;

    @Transactional
    public DetallePonderadoClasificador guardarDetalle(DetallePonderadoClasificadorDTO dto) {
        validarPorcentajes(dto);
        
        // Buscar el ClasificadorPresupuestal por ID
        ClasificadorPresupuestal clasificador = clasificadorRepository.findById(dto.getClasificadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Clasificador Presupuestal no encontrado con ID: " + dto.getClasificadorId()));
        
        // Mapear DTO -> Entidad
        DetallePonderadoClasificador detalle = new DetallePonderadoClasificador();
        detalle.setClasificadorPresupuestal(clasificador);
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

    private void validarPorcentajes(DetallePonderadoClasificadorDTO dto) {
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
    public DetallePonderadoClasificadorDTO convertirEntidadADTO(DetallePonderadoClasificador detalle) {
        return detallePonderadoMapper.toDTO(detalle);
    }

    // Otros métodos CRUD y de consulta
}
