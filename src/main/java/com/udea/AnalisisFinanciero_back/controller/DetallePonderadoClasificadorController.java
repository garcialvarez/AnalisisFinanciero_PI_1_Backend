package com.udea.AnalisisFinanciero_back.controller;

import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoClasificadorDTO;
import com.udea.AnalisisFinanciero_back.entity.DetallePonderadoClasificador;
import com.udea.AnalisisFinanciero_back.service.DetallePonderadoClasificadorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/detalle-ponderado-clasificador")
@Tag(name = "Detalle Ponderado Clasificador", description = "Gestión de porcentajes por clasificador presupuestal")
public class DetallePonderadoClasificadorController {

    private final DetallePonderadoClasificadorService service;

    @Autowired
    public DetallePonderadoClasificadorController(DetallePonderadoClasificadorService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('GESTIONAR_GASTOS')")
    public ResponseEntity<DetallePonderadoClasificador> crear(@RequestBody DetallePonderadoClasificadorDTO dto) {
        DetallePonderadoClasificador detalle = service.guardarDetalle(dto);
        return ResponseEntity.ok(detalle);
    }

    // Otros métodos CRUD protegidos por el mismo permiso
}
