package com.udea.AnalisisFinanciero_back.controller;

import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoCentroGestorDTO;
import com.udea.AnalisisFinanciero_back.entity.DetallePonderadoCentroGestor;
import com.udea.AnalisisFinanciero_back.service.DetallePonderadoCentroGestorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/detalle-ponderado-centro-gestor")
@Tag(name = "Detalle Ponderado Centro Gestor", description = "Gestión de porcentajes por centro gestor")
@SecurityRequirement(name = "Bearer Authentication")
public class DetallePonderadoCentroGestorController {

    private final DetallePonderadoCentroGestorService service;

    @Autowired
    public DetallePonderadoCentroGestorController(DetallePonderadoCentroGestorService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('GESTIONAR_GASTOS')")
    public ResponseEntity<DetallePonderadoCentroGestorDTO> crear(@RequestBody DetallePonderadoCentroGestorDTO dto) {
        DetallePonderadoCentroGestor detalle = service.guardarDetalle(dto);
        DetallePonderadoCentroGestorDTO responseDTO = service.convertirEntidadADTO(detalle);
        return ResponseEntity.ok(responseDTO);
    }

    // Otros métodos CRUD protegidos por el mismo permiso
}
