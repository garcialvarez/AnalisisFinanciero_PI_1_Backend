package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoCentroGestorDTO;
import com.udea.AnalisisFinanciero_back.entity.CentroGestor;
import com.udea.AnalisisFinanciero_back.entity.DetallePonderadoCentroGestor;
import com.udea.AnalisisFinanciero_back.exceptions.ResourceNotFoundException;
import com.udea.AnalisisFinanciero_back.exceptions.ValidationException;
import com.udea.AnalisisFinanciero_back.mapper.DetallePonderadoMapper;
import com.udea.AnalisisFinanciero_back.repository.CentroGestorRepository;
import com.udea.AnalisisFinanciero_back.repository.DetallePonderadoCentroGestorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;


//Servicio para operaciones de DetallePonderadoCentroGestor.
@Service
@RequiredArgsConstructor
@Slf4j
public class DetallePonderadoCentroGestorService 
    extends BaseDetallePonderadoService<DetallePonderadoCentroGestor, DetallePonderadoCentroGestorDTO> {

    private final DetallePonderadoCentroGestorRepository repository;
    private final CentroGestorRepository centroGestorRepository;
    private final DetallePonderadoMapper detallePonderadoMapper;

    @Override
    protected JpaRepository<DetallePonderadoCentroGestor, Integer> getRepository() {
        return repository;
    }

    @Override
    protected DetallePonderadoCentroGestorDTO mapToDTO(DetallePonderadoCentroGestor entity) {
        return detallePonderadoMapper.toDTO(entity);
    }

    @Override
    protected DetallePonderadoCentroGestor mapToEntity(DetallePonderadoCentroGestorDTO dto) {
        DetallePonderadoCentroGestor entity = createNewEntity();
        
        // Buscar y asignar el CentroGestor
        CentroGestor centroGestor = centroGestorRepository.findById(dto.getCentroGestorId())
                .orElseThrow(() -> new ResourceNotFoundException("Centro Gestor no encontrado con ID: " + dto.getCentroGestorId()));
        
        entity.setCentroGestor(centroGestor);
        
        // Mapear valores mensuales
        updateEntityFromDTO(entity, dto);
        
        return entity;
    }

    @Override
    protected DetallePonderadoCentroGestor createNewEntity() {
        return new DetallePonderadoCentroGestor();
    }

    @Override
    protected String getEntityName() {
        return "DetallePonderadoCentroGestor";
    }

    /**
     * Validaciones específicas para DetallePonderadoCentroGestor.
     * Sobrescribe el método base para agregar validación de porcentajes.
     */
    @Override
    protected void validateDTOForCreate(DetallePonderadoCentroGestorDTO dto) {
        super.validateDTOForCreate(dto);
        validateCentroGestorId(dto);
        validatePercentages(dto);
    }

    @Override
    protected void validateDTOForUpdate(DetallePonderadoCentroGestorDTO dto) {
        super.validateDTOForUpdate(dto);
        validateCentroGestorId(dto);
        validatePercentages(dto);
    }


    //Valida que el ID del Centro Gestor sea válido.
    private void validateCentroGestorId(DetallePonderadoCentroGestorDTO dto) {
        if (dto.getCentroGestorId() == null) {
            throw new ValidationException(Map.of("centroGestorId", "El ID del Centro Gestor no puede ser nulo"));
        }
        if (dto.getCentroGestorId() <= 0) {
            throw new ValidationException(Map.of("centroGestorId", "El ID del Centro Gestor debe ser un número positivo"));
        }
    }

    /**
     * Valida que los porcentajes sumen 100%.
     * Específico para el dominio de DetallePonderadoCentroGestor.
     */
    private void validatePercentages(DetallePonderadoCentroGestorDTO dto) {
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

    //Actualiza campos específicos de DetallePonderadoCentroGestor.
    @Override
    protected void updateEntityFromDTO(DetallePonderadoCentroGestor entity, DetallePonderadoCentroGestorDTO dto) {
        super.updateEntityFromDTO(entity, dto);
        
        // Si la entidad ya existe y se está actualizando, mantener la referencia al CentroGestor
        // o actualizarla si es necesario
        if (entity.getCentroGestor() == null || 
            !entity.getCentroGestor().getCentroGestorId().equals(dto.getCentroGestorId())) {
            CentroGestor centroGestor = centroGestorRepository.findById(dto.getCentroGestorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Centro Gestor no encontrado con ID: " + dto.getCentroGestorId()));
            entity.setCentroGestor(centroGestor);
        }
    }

 
    @Deprecated
    @Transactional
    public DetallePonderadoCentroGestor guardarDetalle(DetallePonderadoCentroGestorDTO dto) {
        log.warn("Usando método deprecado guardarDetalle. Usar create() en su lugar.");
        DetallePonderadoCentroGestorDTO result = create(dto);
        return mapToEntity(result);
    }

    @Deprecated
    public DetallePonderadoCentroGestorDTO convertirEntidadADTO(DetallePonderadoCentroGestor detalle) {
        log.warn("Usando método deprecado convertirEntidadADTO. Usar mapToDTO() en su lugar.");
        return mapToDTO(detalle);
    }
}
