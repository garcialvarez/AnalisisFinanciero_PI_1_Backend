package com.udea.AnalisisFinanciero_back.controller;

import com.udea.AnalisisFinanciero_back.DTO.DetallePonderadoClasificadorDTO;
import com.udea.AnalisisFinanciero_back.entity.DetallePonderadoClasificador;
import com.udea.AnalisisFinanciero_back.service.DetallePonderadoClasificadorService;
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
@RequestMapping("/detalle-ponderado-clasificador")
@Tag(name = "Detalle Ponderado Clasificador", description = "Gestión de porcentajes por clasificador presupuestal")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@Validated
public class DetallePonderadoClasificadorController {

    private final DetallePonderadoClasificadorService service;

    @PostMapping
    @Operation(summary = "Crear detalle ponderado para clasificador", 
               description = "Crea un nuevo detalle de ponderación mensual para un clasificador presupuestal")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Detalle ponderado creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Clasificador presupuestal no encontrado")
    })
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('GESTIONAR_GASTOS')")
    public ResponseEntity<DetallePonderadoClasificadorDTO> crear(
            @Valid @RequestBody DetallePonderadoClasificadorDTO dto) {
        DetallePonderadoClasificador detalle = service.guardarDetalle(dto);
        DetallePonderadoClasificadorDTO responseDTO = service.convertirEntidadADTO(detalle);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
