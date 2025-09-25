package com.udea.AnalisisFinanciero_back.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermisoDTO {
    private Long permisoId;
    private String nombrePermiso;
    private String descripcion;
}

