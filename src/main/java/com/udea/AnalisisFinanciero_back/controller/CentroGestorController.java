package com.udea.AnalisisFinanciero_back.controller;

import com.udea.AnalisisFinanciero_back.DTO.CentroGestorDTO;
import com.udea.AnalisisFinanciero_back.DTO.response.CentroGestorConDetallesDTO;
import com.udea.AnalisisFinanciero_back.service.CentroGestorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Centros Gestores", description = "API para gestión de centros gestores")
@RestController
@RequestMapping("/centros-gestores")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Validated
public class CentroGestorController {

    private final CentroGestorService centroGestorService;

    @Operation(summary = "Buscar centro gestor por código", 
               description = "Obtiene los datos básicos de un centro gestor por su código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Centro gestor encontrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Código de centro gestor inválido"),
        @ApiResponse(responseCode = "404", description = "Centro gestor no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/{codigo}")
    @PreAuthorize("hasAuthority('GESTIONAR_GASTOS') or hasAuthority('VER_REPORTES')")
    public ResponseEntity<CentroGestorDTO> buscarPorCodigo(
            @Parameter(description = "Código del centro gestor", example = "CG001")
            @PathVariable @NotBlank(message = "El código del centro gestor no puede estar vacío") String codigo) {
        
        CentroGestorDTO centroGestor = centroGestorService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(centroGestor);
    }

    @Operation(summary = "Buscar centro gestor con detalles por código", 
               description = "Obtiene un centro gestor con sus detalles ponderados por código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Centro gestor con detalles encontrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Código de centro gestor inválido"),
        @ApiResponse(responseCode = "404", description = "Centro gestor no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/{codigo}/detalles")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('GESTIONAR_GASTOS') or hasAuthority('VER_REPORTES')")
    public ResponseEntity<CentroGestorConDetallesDTO> buscarPorCodigoConDetalles(
            @Parameter(description = "Código del centro gestor", example = "CG001")
            @PathVariable @NotBlank(message = "El código del centro gestor no puede estar vacío") String codigo) {
        
        CentroGestorConDetallesDTO centroGestor = 
                centroGestorService.buscarPorCodigoConDetalles(codigo);
        return ResponseEntity.ok(centroGestor);
    }

    @Operation(summary = "Listar todos los centros gestores con detalles", 
               description = "Obtiene todos los centros gestores con sus detalles ponderados de forma paginada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de centros gestores obtenida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/detalles")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('GESTIONAR_GASTOS') or hasAuthority('VER_REPORTES')")
    public ResponseEntity<Page<CentroGestorConDetallesDTO>> obtenerTodosConDetalles(
            @Parameter(description = "Número de página (inicia en 0)", example = "0")
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "El número de página debe ser mayor o igual a 0") int page,
            
            @Parameter(description = "Tamaño de página", example = "10")
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "El tamaño de página debe ser mayor a 0") int size,
            
            @Parameter(description = "Campo para ordenar", example = "codigo")
            @RequestParam(defaultValue = "codigo") @NotBlank(message = "El campo de ordenamiento no puede estar vacío") String sortBy,
            
            @Parameter(description = "Dirección del ordenamiento (asc/desc)", example = "asc")
            @RequestParam(defaultValue = "asc") @NotBlank(message = "La dirección de ordenamiento no puede estar vacía") String sortDir) {
        
        Page<CentroGestorConDetallesDTO> centrosGestores = 
                centroGestorService.obtenerTodosConDetalles(page, size, sortBy, sortDir);
        
        return ResponseEntity.ok(centrosGestores);
    }
}