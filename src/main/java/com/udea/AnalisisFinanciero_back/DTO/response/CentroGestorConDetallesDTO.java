package com.udea.AnalisisFinanciero_back.DTO.response;

import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoCentroGestorDTO;
import lombok.Data;
import java.util.List;

@Data
public class CentroGestorConDetallesDTO {
    private Integer centroGestorId;
    private String codigo;
    private String nombreCentroGestor;
    private List<DetallePonderadoCentroGestorDTO> detallesPonderados;
}