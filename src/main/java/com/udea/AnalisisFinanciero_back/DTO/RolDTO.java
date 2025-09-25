package com.udea.AnalisisFinanciero_back.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolDTO {
    private Integer rolId;
    private String nombreRol;
    private String descripcion;
    private Set<PermisoDTO> permisos = new HashSet<>();
}

