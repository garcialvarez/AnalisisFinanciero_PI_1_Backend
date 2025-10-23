package com.udea.AnalisisFinanciero_back.mapper;

import com.udea.AnalisisFinanciero_back.DTO.ClasificadorPresupuestalDTO;
import com.udea.AnalisisFinanciero_back.DTO.response.ClasificadorConDetallesDTO;
import com.udea.AnalisisFinanciero_back.entity.ClasificadorPresupuestal;
import org.springframework.stereotype.Component;

@Component
public class ClasificadorPresupuestalMapper {

    /**
     * Convierte entidad a DTO básico (sin detalles)
     */
    public ClasificadorPresupuestalDTO toDTO(ClasificadorPresupuestal clasificador) {
        if (clasificador == null) return null;
        
        ClasificadorPresupuestalDTO dto = new ClasificadorPresupuestalDTO();
        dto.setClasificadorId(clasificador.getClasificadorId());
        dto.setCodigo(clasificador.getCodigo());
        dto.setNombreClasificador(clasificador.getNombreClasificador());
        dto.setDescripcion(clasificador.getDescripcion());
        dto.setClasePospre(clasificador.getClasePospre());
        dto.setIdEstado(clasificador.getEstadoClasificador() != null ? 
                       clasificador.getEstadoClasificador().getIdEstado() : null);
        dto.setCentroGestorId(clasificador.getCentroGestor() != null ? 
                             clasificador.getCentroGestor().getCentroGestorId() : null);
        return dto;
    }

    /**
     * Convierte entidad a DTO con información extendida (sin detalles ponderados)
     */
    public ClasificadorConDetallesDTO toConDetallesDTO(ClasificadorPresupuestal clasificador) {
        if (clasificador == null) return null;
        
        ClasificadorConDetallesDTO dto = new ClasificadorConDetallesDTO();
        dto.setClasificadorId(clasificador.getClasificadorId());
        dto.setCodigo(clasificador.getCodigo());
        dto.setNombreClasificador(clasificador.getNombreClasificador());
        dto.setDescripcion(clasificador.getDescripcion());
        dto.setClasePospre(clasificador.getClasePospre());
        dto.setIdEstado(clasificador.getEstadoClasificador() != null ? 
                       clasificador.getEstadoClasificador().getIdEstado() : null);
        dto.setNombreEstado(clasificador.getEstadoClasificador() != null ? 
                           clasificador.getEstadoClasificador().getNombreEstado() : null);
        dto.setCentroGestorId(clasificador.getCentroGestor() != null ? 
                             clasificador.getCentroGestor().getCentroGestorId() : null);
        dto.setNombreCentroGestor(clasificador.getCentroGestor() != null ? 
                                 clasificador.getCentroGestor().getNombreCentroGestor() : null);        
        
        return dto;
    }
}