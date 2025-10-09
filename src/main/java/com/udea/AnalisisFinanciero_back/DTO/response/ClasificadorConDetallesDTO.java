package com.udea.AnalisisFinanciero_back.DTO.response;

import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoClasificadorDTO;
import lombok.Data;
import java.util.List;

@Data
public class ClasificadorConDetallesDTO {
    private Integer clasificadorId;
    private String codigo;
    private String nombreClasificador;
    private String descripcion;
    private String clasePospre;
    private Integer idEstado;
    private String nombreEstado;
    private Integer centroGestorId;
    private String nombreCentroGestor;
    private List<DetallePonderadoClasificadorDTO> detallesPonderados;
}