package com.udea.AnalisisFinanciero_back.controller;

import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoCentroGestorDTO;
import com.udea.AnalisisFinanciero_back.entity.DetallePonderadoCentroGestor;
import com.udea.AnalisisFinanciero_back.service.DetallePonderadoCentroGestorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/detalle-ponderado-centro-gestor")
@Tag(name = "Detalle Ponderado Centro Gestor", description = "Gestión de porcentajes por centro gestor")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@Validated
public class DetallePonderadoCentroGestorController {

    private final DetallePonderadoCentroGestorService service;

    @PostMapping
    @Operation(summary = "Crear detalle ponderado para centro gestor", 
               description = "Crea un nuevo detalle de ponderación mensual para un centro gestor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Detalle ponderado creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Centro gestor no encontrado")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('GESTIONAR_GASTOS')")
    public ResponseEntity<DetallePonderadoCentroGestorDTO> crear(
            @Valid @RequestBody DetallePonderadoCentroGestorDTO dto) {
        DetallePonderadoCentroGestor detalle = service.guardarDetalle(dto);
        DetallePonderadoCentroGestorDTO responseDTO = service.convertirEntidadADTO(detalle);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
