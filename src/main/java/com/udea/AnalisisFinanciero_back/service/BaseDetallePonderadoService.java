package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.exceptions.ResourceNotFoundException;
import com.udea.AnalisisFinanciero_back.exceptions.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Slf4j
public abstract class BaseDetallePonderadoService<T extends DetallePonderadoEntity, D extends DetallePonderadoDTO> {

 
    protected abstract JpaRepository<T, Integer> getRepository();

    protected abstract D mapToDTO(T entity);

    protected abstract T mapToEntity(D dto);

    protected abstract T createNewEntity();

    protected abstract String getEntityName();

    @Transactional(readOnly = true)
    public Page<D> findAll(Pageable pageable) {
        log.info("Obteniendo todos los {} con paginación: {}", getEntityName(), pageable);
        try {
            Page<T> entities = getRepository().findAll(pageable);
            return entities.map(this::mapToDTO);
        } catch (Exception e) {
            log.error("Error al obtener {}: {}", getEntityName(), e.getMessage());
            throw new RuntimeException("Error al obtener " + getEntityName(), e);
        }
    }
    //Busca por ID.
    @Transactional(readOnly = true)
    public D findById(Integer id) {
        validateId(id);
        log.info("Buscando {} por ID: {}", getEntityName(), id);
        
        T entity = getRepository().findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(getEntityName() + " no encontrado con ID: " + id));
        
        return mapToDTO(entity);
    }
    //Crea un nuevo registro.
    @Transactional
    public D create(D dto) {
        validateDTOForCreate(dto);
        log.info("Creando nuevo {}", getEntityName());
        
        T entity = mapToEntity(dto);
        calculateAndSetTotal(entity);
        
        T savedEntity = getRepository().save(entity);
        log.info("{} creado exitosamente con ID: {}", getEntityName(), savedEntity.hashCode());
        
        return mapToDTO(savedEntity);
    }

    //Actualiza un registro existente.
    @Transactional
    public D update(Integer id, D dto) {
        validateId(id);
        validateDTOForUpdate(dto);
        log.info("Actualizando {} con ID: {}", getEntityName(), id);
        
        T existingEntity = getRepository().findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(getEntityName() + " no encontrado con ID: " + id));
        
        updateEntityFromDTO(existingEntity, dto);
        calculateAndSetTotal(existingEntity);
        
        T updatedEntity = getRepository().save(existingEntity);
        log.info("{} actualizado exitosamente", getEntityName());
        
        return mapToDTO(updatedEntity);
    }

    //Elimina un registro por ID.
    @Transactional
    public void deleteById(Integer id) {
        validateId(id);
        log.info("Eliminando {} con ID: {}", getEntityName(), id);
        
        if (!getRepository().existsById(id)) {
            throw new ResourceNotFoundException(getEntityName() + " no encontrado con ID: " + id);
        }
        
        getRepository().deleteById(id);
        log.info("{} eliminado exitosamente", getEntityName());
    }

    /**
     * Actualiza los campos de la entidad existente con los valores del DTO.
     * Template Method Pattern - puede ser sobrescrito por subclases si necesitan lógica adicional.
     */
    protected void updateEntityFromDTO(T entity, D dto) {
        entity.setEnero(dto.getEnero());
        entity.setFebrero(dto.getFebrero());
        entity.setMarzo(dto.getMarzo());
        entity.setAbril(dto.getAbril());
        entity.setMayo(dto.getMayo());
        entity.setJunio(dto.getJunio());
        entity.setJulio(dto.getJulio());
        entity.setAgosto(dto.getAgosto());
        entity.setSeptiembre(dto.getSeptiembre());
        entity.setOctubre(dto.getOctubre());
        entity.setNoviembre(dto.getNoviembre());
        entity.setDiciembre(dto.getDiciembre());
    }

    //Calcula y establece el total basado en los valores mensuales.
    protected void calculateAndSetTotal(T entity) {
        BigDecimal total = BigDecimal.ZERO
            .add(entity.getEnero() != null ? entity.getEnero() : BigDecimal.ZERO)
            .add(entity.getFebrero() != null ? entity.getFebrero() : BigDecimal.ZERO)
            .add(entity.getMarzo() != null ? entity.getMarzo() : BigDecimal.ZERO)
            .add(entity.getAbril() != null ? entity.getAbril() : BigDecimal.ZERO)
            .add(entity.getMayo() != null ? entity.getMayo() : BigDecimal.ZERO)
            .add(entity.getJunio() != null ? entity.getJunio() : BigDecimal.ZERO)
            .add(entity.getJulio() != null ? entity.getJulio() : BigDecimal.ZERO)
            .add(entity.getAgosto() != null ? entity.getAgosto() : BigDecimal.ZERO)
            .add(entity.getSeptiembre() != null ? entity.getSeptiembre() : BigDecimal.ZERO)
            .add(entity.getOctubre() != null ? entity.getOctubre() : BigDecimal.ZERO)
            .add(entity.getNoviembre() != null ? entity.getNoviembre() : BigDecimal.ZERO)
            .add(entity.getDiciembre() != null ? entity.getDiciembre() : BigDecimal.ZERO);
        
        entity.setTotal(total);
    }

    // Métodos de validación comunes

    protected void validateId(Integer id) {
        if (id == null) {
            throw new ValidationException(Map.of("id", "El ID no puede ser nulo"));
        }
        if (id <= 0) {
            throw new ValidationException(Map.of("id", "El ID debe ser un número positivo"));
        }
    }

    /**
     * Validaciones específicas para creación.
     * Template Method Pattern - puede ser sobrescrito por subclases.
     */
    protected void validateDTOForCreate(D dto) {
        if (dto == null) {
            throw new ValidationException(Map.of("dto", "El " + getEntityName() + " no puede ser nulo"));
        }
        validateMonthlyValues(dto);
    }

    /**
     * Validaciones específicas para actualización.
     * Template Method Pattern - puede ser sobrescrito por subclases.
     */
    protected void validateDTOForUpdate(D dto) {
        validateDTOForCreate(dto);
    }

    /**
     * Valida que los valores mensuales no sean negativos.
     */
    protected void validateMonthlyValues(D dto) {
        validateNonNegativeValue(dto.getEnero(), "enero");
        validateNonNegativeValue(dto.getFebrero(), "febrero");
        validateNonNegativeValue(dto.getMarzo(), "marzo");
        validateNonNegativeValue(dto.getAbril(), "abril");
        validateNonNegativeValue(dto.getMayo(), "mayo");
        validateNonNegativeValue(dto.getJunio(), "junio");
        validateNonNegativeValue(dto.getJulio(), "julio");
        validateNonNegativeValue(dto.getAgosto(), "agosto");
        validateNonNegativeValue(dto.getSeptiembre(), "septiembre");
        validateNonNegativeValue(dto.getOctubre(), "octubre");
        validateNonNegativeValue(dto.getNoviembre(), "noviembre");
        validateNonNegativeValue(dto.getDiciembre(), "diciembre");
    }

    //Valida que un valor no sea negativo
    protected void validateNonNegativeValue(BigDecimal value, String fieldName) {
        if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException(Map.of(fieldName, "El valor para " + fieldName + " no puede ser negativo"));
        }
    }
}