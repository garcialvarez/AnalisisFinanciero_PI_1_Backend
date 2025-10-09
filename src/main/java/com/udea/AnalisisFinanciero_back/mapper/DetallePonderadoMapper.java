package com.udea.AnalisisFinanciero_back.mapper;

import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoClasificadorDTO;
import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoCentroGestorDTO;
import com.udea.AnalisisFinanciero_back.entity.DetallePonderadoClasificador;
import com.udea.AnalisisFinanciero_back.entity.DetallePonderadoCentroGestor;
import org.springframework.stereotype.Component;

@Component
public class DetallePonderadoMapper {

    /**
     * Convierte DetallePonderadoClasificador entidad a DTO
     */
    public DetallePonderadoClasificadorDTO toDTO(DetallePonderadoClasificador detalle) {
        if (detalle == null) return null;
        
        DetallePonderadoClasificadorDTO dto = new DetallePonderadoClasificadorDTO();
        dto.setDetallePonderadoId(detalle.getDetallePonderadoId());
        dto.setClasificadorId(detalle.getClasificadorPresupuestal() != null ? 
                             detalle.getClasificadorPresupuestal().getClasificadorId() : null);
        dto.setEnero(detalle.getEnero());
        dto.setFebrero(detalle.getFebrero());
        dto.setMarzo(detalle.getMarzo());
        dto.setAbril(detalle.getAbril());
        dto.setMayo(detalle.getMayo());
        dto.setJunio(detalle.getJunio());
        dto.setJulio(detalle.getJulio());
        dto.setAgosto(detalle.getAgosto());
        dto.setSeptiembre(detalle.getSeptiembre());
        dto.setOctubre(detalle.getOctubre());
        dto.setNoviembre(detalle.getNoviembre());
        dto.setDiciembre(detalle.getDiciembre());
        dto.setTotal(detalle.getTotal());
        return dto;
    }

    /**
     * Convierte DetallePonderadoCentroGestor entidad a DTO
     */
    public DetallePonderadoCentroGestorDTO toDTO(DetallePonderadoCentroGestor detalle) {
        if (detalle == null) return null;
        
        DetallePonderadoCentroGestorDTO dto = new DetallePonderadoCentroGestorDTO();
        dto.setDetallePonderadoId(detalle.getDetallePonderadoId());
        dto.setCentroGestorId(detalle.getCentroGestor() != null ? 
                             detalle.getCentroGestor().getCentroGestorId() : null);
        dto.setEnero(detalle.getEnero());
        dto.setFebrero(detalle.getFebrero());
        dto.setMarzo(detalle.getMarzo());
        dto.setAbril(detalle.getAbril());
        dto.setMayo(detalle.getMayo());
        dto.setJunio(detalle.getJunio());
        dto.setJulio(detalle.getJulio());
        dto.setAgosto(detalle.getAgosto());
        dto.setSeptiembre(detalle.getSeptiembre());
        dto.setOctubre(detalle.getOctubre());
        dto.setNoviembre(detalle.getNoviembre());
        dto.setDiciembre(detalle.getDiciembre());
        dto.setTotal(detalle.getTotal());
        return dto;
    }
}