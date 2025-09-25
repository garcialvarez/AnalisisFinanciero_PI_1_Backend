package com.udea.AnalisisFinanciero_back.mapper;

import com.udea.AnalisisFinanciero_back.DTO.PermisoDTO;
import com.udea.AnalisisFinanciero_back.DTO.RolDTO;
import com.udea.AnalisisFinanciero_back.entity.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RolMapper {

    @Autowired
    private PermisoMapper permisoMapper;

    public RolDTO toDto(Rol rol) {
        if (rol == null) {
            return null;
        }
        RolDTO dto = new RolDTO();
        dto.setRolId(rol.getRolId());
        dto.setNombreRol(rol.getNombreRol());
        dto.setDescripcion(rol.getDescripcion());

        // Mapeamos los permisos si existen
        if (rol.getPermisos() != null) {
            Set<PermisoDTO> permisosDTO = rol.getPermisos().stream()
                    .map(permisoMapper::toDto)
                    .collect(Collectors.toSet());
            dto.setPermisos(permisosDTO);
        }

        return dto;
    }

    public Rol toEntity(RolDTO rolDTO) {
        if (rolDTO == null) {
            return null;
        }
        Rol entity = new Rol();
        entity.setRolId(rolDTO.getRolId());
        entity.setNombreRol(rolDTO.getNombreRol());
        entity.setDescripcion(rolDTO.getDescripcion());
        return entity;
    }

    public List<RolDTO> toDtoList(List<Rol> roles) {
        return roles.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
