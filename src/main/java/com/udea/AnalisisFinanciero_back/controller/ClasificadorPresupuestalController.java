package com.udea.AnalisisFinanciero_back.controller;

import com.udea.AnalisisFinanciero_back.DTO.ClasificadorPresupuestalDTO;
import com.udea.AnalisisFinanciero_back.DTO.response.ClasificadorConDetallesDTO;
import com.udea.AnalisisFinanciero_back.service.ClasificadorPresupuestalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Clasificadores Presupuestales", description = "API para gestión de clasificadores presupuestales")
@RestController
@RequestMapping("/clasificadores")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ClasificadorPresupuestalController {

    private final ClasificadorPresupuestalService clasificadorService;

    @Operation(summary = "Buscar clasificador por código", 
               description = "Obtiene los datos básicos de un clasificador presupuestal por su código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clasificador encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Clasificador no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/{codigo}")
    @PreAuthorize("hasAuthority('GESTIONAR_CLASIFICACION') or hasAuthority('VER_REPORTES')")
    public ResponseEntity<ClasificadorPresupuestalDTO> buscarPorCodigo(
            @Parameter(description = "Código del clasificador presupuestal", example = "CP001")
            @PathVariable String codigo) {
        
        ClasificadorPresupuestalDTO clasificador = clasificadorService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(clasificador);
    }

    @Operation(summary = "Buscar clasificador con detalles por código", 
               description = "Obtiene un clasificador presupuestal con sus detalles ponderados por código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clasificador con detalles encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Clasificador no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/{codigo}/detalles")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('GESTIONAR_CLASIFICACION') or hasAuthority('VER_REPORTES')")
    public ResponseEntity<ClasificadorConDetallesDTO> buscarPorCodigoConDetalles(
            @Parameter(description = "Código del clasificador presupuestal", example = "CP001")
            @PathVariable String codigo) {
        
        ClasificadorConDetallesDTO clasificador = 
                clasificadorService.buscarPorCodigoConDetalles(codigo);
        return ResponseEntity.ok(clasificador);
    }

    @Operation(summary = "Listar todos los clasificadores con detalles", 
               description = "Obtiene todos los clasificadores presupuestales con sus detalles ponderados de forma paginada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clasificadores obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/detalles")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('GESTIONAR_CLASIFICACION') or hasAuthority('VER_REPORTES')")
    public ResponseEntity<Page<ClasificadorConDetallesDTO>> obtenerTodosConDetalles(
            @Parameter(description = "Número de página (inicia en 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Tamaño de página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Campo para ordenar", example = "codigo")
            @RequestParam(defaultValue = "codigo") String sortBy,
            
            @Parameter(description = "Dirección del ordenamiento (asc/desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        // Crear sort
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        // Crear pageable
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ClasificadorConDetallesDTO> clasificadores = 
                clasificadorService.obtenerTodosConDetalles(pageable);
        
        return ResponseEntity.ok(clasificadores);
    }
}