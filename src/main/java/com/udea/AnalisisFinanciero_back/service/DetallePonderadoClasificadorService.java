package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoClasificadorDTO;
import com.udea.AnalisisFinanciero_back.entity.DetallePonderadoClasificador;
import com.udea.AnalisisFinanciero_back.repository.DetallePonderadoClasificadorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DetallePonderadoClasificadorService {
    private final DetallePonderadoClasificadorRepository repository;

    @Autowired
    public DetallePonderadoClasificadorService(DetallePonderadoClasificadorRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public DetallePonderadoClasificador guardarDetalle(DetallePonderadoClasificadorDTO dto) {
        validarPorcentajes(dto);
        // Aquí iría el mapeo DTO -> Entidad
        DetallePonderadoClasificador detalle = new DetallePonderadoClasificador();
        detalle.setClasificadorPresupuestal(null); // Asignar entidad según lógica
        detalle.setEnero(dto.getEnero());
        detalle.setFebrero(dto.getFebrero());
        detalle.setMarzo(dto.getMarzo());
        detalle.setAbril(dto.getAbril());
        detalle.setMayo(dto.getMayo());
        detalle.setJunio(dto.getJunio());
        detalle.setJulio(dto.getJulio());
        detalle.setAgosto(dto.getAgosto());
        detalle.setSeptiembre(dto.getSeptiembre());
        detalle.setOctubre(dto.getOctubre());
        detalle.setNoviembre(dto.getNoviembre());
        detalle.setDiciembre(dto.getDiciembre());
        detalle.setTotal(dto.getTotal());
        return repository.save(detalle);
    }

    private void validarPorcentajes(DetallePonderadoClasificadorDTO dto) {
        BigDecimal suma = dto.getEnero()
                .add(dto.getFebrero())
                .add(dto.getMarzo())
                .add(dto.getAbril())
                .add(dto.getMayo())
                .add(dto.getJunio())
                .add(dto.getJulio())
                .add(dto.getAgosto())
                .add(dto.getSeptiembre())
                .add(dto.getOctubre())
                .add(dto.getNoviembre())
                .add(dto.getDiciembre());
        if (suma.compareTo(new BigDecimal("100.00")) != 0) {
            throw new IllegalArgumentException("La suma de los porcentajes de los meses debe ser igual a 100%");
        }
        if (dto.getTotal().compareTo(new BigDecimal("100.00")) != 0) {
            throw new IllegalArgumentException("El total debe ser igual a 100%");
        }
    }

    // Otros métodos CRUD y de consulta
}
