package com.udea.AnalisisFinanciero_back.DTO;

import lombok.Data;

@Data
public class ClasificadorPresupuestalDTO {
    private Integer clasificadorId;
    private String codigo;
    private String nombreClasificador;
    private String descripcion;
    private String clasePospre;
    private Integer idEstado;
    private Integer centroGestorId;
}
