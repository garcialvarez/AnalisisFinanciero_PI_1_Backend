package com.udea.AnalisisFinanciero_back.mapper;

import com.udea.AnalisisFinanciero_back.DTO.PermisoDTO;
import com.udea.AnalisisFinanciero_back.entity.Permiso;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PermisoMapper {

    public PermisoDTO toDto(Permiso permiso) {
        if (permiso == null) {
            return null;
        }
        PermisoDTO dto = new PermisoDTO();
        dto.setPermisoId(permiso.getPermisoId());
        dto.setNombrePermiso(permiso.getNombrePermiso());
        dto.setDescripcion(permiso.getDescripcion());
        return dto;
    }

    public Permiso toEntity(PermisoDTO permisoDTO) {
        if (permisoDTO == null) {
            return null;
        }
        Permiso entity = new Permiso();
        entity.setPermisoId(permisoDTO.getPermisoId());
        entity.setNombrePermiso(permisoDTO.getNombrePermiso());
        entity.setDescripcion(permisoDTO.getDescripcion());
        return entity;
    }

    public List<PermisoDTO> toDtoList(List<Permiso> permisos) {
        return permisos.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

