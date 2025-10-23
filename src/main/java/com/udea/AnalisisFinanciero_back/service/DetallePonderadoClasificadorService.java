package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoClasificadorDTO;
import com.udea.AnalisisFinanciero_back.entity.ClasificadorPresupuestal;
import com.udea.AnalisisFinanciero_back.entity.DetallePonderadoClasificador;
import com.udea.AnalisisFinanciero_back.exceptions.ResourceNotFoundException;
import com.udea.AnalisisFinanciero_back.exceptions.ValidationException;
import com.udea.AnalisisFinanciero_back.mapper.DetallePonderadoMapper;
import com.udea.AnalisisFinanciero_back.repository.ClasificadorPresupuestalRepository;
import com.udea.AnalisisFinanciero_back.repository.DetallePonderadoClasificadorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

//Servicio para operaciones de DetallePonderadoClasificador.
@Service
@RequiredArgsConstructor
@Slf4j
public class DetallePonderadoClasificadorService 
    extends BaseDetallePonderadoService<DetallePonderadoClasificador, DetallePonderadoClasificadorDTO> {

    private final DetallePonderadoClasificadorRepository repository;
    private final ClasificadorPresupuestalRepository clasificadorRepository;
    private final DetallePonderadoMapper detallePonderadoMapper;

    @Override
    protected JpaRepository<DetallePonderadoClasificador, Integer> getRepository() {
        return repository;
    }

    @Override
    protected DetallePonderadoClasificadorDTO mapToDTO(DetallePonderadoClasificador entity) {
        return detallePonderadoMapper.toDTO(entity);
    }

    @Override
    protected DetallePonderadoClasificador mapToEntity(DetallePonderadoClasificadorDTO dto) {
        DetallePonderadoClasificador entity = createNewEntity();
        
        // Buscar y asignar el ClasificadorPresupuestal
        ClasificadorPresupuestal clasificador = clasificadorRepository.findById(dto.getClasificadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Clasificador Presupuestal no encontrado con ID: " + dto.getClasificadorId()));
        
        entity.setClasificadorPresupuestal(clasificador);
        
        // Mapear valores mensuales
        updateEntityFromDTO(entity, dto);
        
        return entity;
    }

    @Override
    protected DetallePonderadoClasificador createNewEntity() {
        return new DetallePonderadoClasificador();
    }

    @Override
    protected String getEntityName() {
        return "DetallePonderadoClasificador";
    }

    /**
     * Validaciones específicas para DetallePonderadoClasificador.
     * Sobrescribe el método base para agregar validación de porcentajes.
     */
    @Override
    protected void validateDTOForCreate(DetallePonderadoClasificadorDTO dto) {
        super.validateDTOForCreate(dto);
        validateClasificadorId(dto);
        validatePercentages(dto);
    }

    @Override
    protected void validateDTOForUpdate(DetallePonderadoClasificadorDTO dto) {
        super.validateDTOForUpdate(dto);
        validateClasificadorId(dto);
        validatePercentages(dto);
    }

    /**
     * Valida que el ID del Clasificador sea válido.
     */
    private void validateClasificadorId(DetallePonderadoClasificadorDTO dto) {
        if (dto.getClasificadorId() == null) {
            throw new ValidationException(Map.of("clasificadorId", "El ID del Clasificador Presupuestal no puede ser nulo"));
        }
        if (dto.getClasificadorId() <= 0) {
            throw new ValidationException(Map.of("clasificadorId", "El ID del Clasificador Presupuestal debe ser un número positivo"));
        }
    }

    /**
     * Valida que los porcentajes sumen 100%.
     * Específico para el dominio de DetallePonderadoClasificador.
     */
    private void validatePercentages(DetallePonderadoClasificadorDTO dto) {
        BigDecimal suma = BigDecimal.ZERO
            .add(dto.getEnero() != null ? dto.getEnero() : BigDecimal.ZERO)
            .add(dto.getFebrero() != null ? dto.getFebrero() : BigDecimal.ZERO)
            .add(dto.getMarzo() != null ? dto.getMarzo() : BigDecimal.ZERO)
            .add(dto.getAbril() != null ? dto.getAbril() : BigDecimal.ZERO)
            .add(dto.getMayo() != null ? dto.getMayo() : BigDecimal.ZERO)
            .add(dto.getJunio() != null ? dto.getJunio() : BigDecimal.ZERO)
            .add(dto.getJulio() != null ? dto.getJulio() : BigDecimal.ZERO)
            .add(dto.getAgosto() != null ? dto.getAgosto() : BigDecimal.ZERO)
            .add(dto.getSeptiembre() != null ? dto.getSeptiembre() : BigDecimal.ZERO)
            .add(dto.getOctubre() != null ? dto.getOctubre() : BigDecimal.ZERO)
            .add(dto.getNoviembre() != null ? dto.getNoviembre() : BigDecimal.ZERO)
            .add(dto.getDiciembre() != null ? dto.getDiciembre() : BigDecimal.ZERO);

        BigDecimal expectedTotal = new BigDecimal("100.00");
        if (suma.compareTo(expectedTotal) != 0) {
            throw new ValidationException(Map.of("suma", "La suma de los porcentajes mensuales debe ser igual a 100%. Suma actual: " + suma));
        }

        if (dto.getTotal() != null && dto.getTotal().compareTo(expectedTotal) != 0) {
            throw new ValidationException(Map.of("total", "El total debe ser igual a 100%. Total actual: " + dto.getTotal()));
        }
    }

    /**
     * Actualiza campos específicos de DetallePonderadoClasificador.
     */
    @Override
    protected void updateEntityFromDTO(DetallePonderadoClasificador entity, DetallePonderadoClasificadorDTO dto) {
        super.updateEntityFromDTO(entity, dto);
        
        // Si la entidad ya existe y se está actualizando, mantener la referencia al Clasificador
        // o actualizarla si es necesario
        if (entity.getClasificadorPresupuestal() == null || 
            !entity.getClasificadorPresupuestal().getClasificadorId().equals(dto.getClasificadorId())) {
            ClasificadorPresupuestal clasificador = clasificadorRepository.findById(dto.getClasificadorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Clasificador Presupuestal no encontrado con ID: " + dto.getClasificadorId()));
            entity.setClasificadorPresupuestal(clasificador);
        }
    }

    @Deprecated
    @Transactional
    public DetallePonderadoClasificador guardarDetalle(DetallePonderadoClasificadorDTO dto) {
        log.warn("Usando método deprecado guardarDetalle. Usar create() en su lugar.");
        DetallePonderadoClasificadorDTO result = create(dto);
        return mapToEntity(result);
    }

    @Deprecated
    public DetallePonderadoClasificadorDTO convertirEntidadADTO(DetallePonderadoClasificador detalle) {
        log.warn("Usando método deprecado convertirEntidadADTO. Usar mapToDTO() en su lugar.");
        return mapToDTO(detalle);
    }
}
