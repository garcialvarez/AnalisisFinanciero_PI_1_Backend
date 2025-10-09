package com.udea.AnalisisFinanciero_back.mapper;

import com.udea.AnalisisFinanciero_back.DTO.CentroGestorDTO;
import com.udea.AnalisisFinanciero_back.DTO.response.CentroGestorConDetallesDTO;
import com.udea.AnalisisFinanciero_back.entity.CentroGestor;
import org.springframework.stereotype.Component;

@Component
public class CentroGestorMapper {

    /**
     * Convierte entidad a DTO básico (sin detalles)
     */
    public CentroGestorDTO toDTO(CentroGestor centroGestor) {
        if (centroGestor == null) return null;
        
        CentroGestorDTO dto = new CentroGestorDTO();
        dto.setCentroGestorId(centroGestor.getCentroGestorId());
        dto.setCodigo(centroGestor.getCodigo());
        dto.setNombreCentroGestor(centroGestor.getNombreCentroGestor());
        return dto;
    }

    /**
     * Convierte entidad a DTO con información extendida (sin detalles ponderados)
     */
    public CentroGestorConDetallesDTO toConDetallesDTO(CentroGestor centroGestor) {
        if (centroGestor == null) return null;
        
        CentroGestorConDetallesDTO dto = new CentroGestorConDetallesDTO();
        dto.setCentroGestorId(centroGestor.getCentroGestorId());
        dto.setCodigo(centroGestor.getCodigo());
        dto.setNombreCentroGestor(centroGestor.getNombreCentroGestor());
        
        // Los detalles ponderados se asignan desde el servicio usando DetallePonderadoMapper
        return dto;
    }
}