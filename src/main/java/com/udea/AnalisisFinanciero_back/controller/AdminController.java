package com.udea.AnalisisFinanciero_back.controller;

import com.udea.AnalisisFinanciero_back.DTO.CreateUsuarioDTO;
import com.udea.AnalisisFinanciero_back.DTO.RolDTO;
import com.udea.AnalisisFinanciero_back.DTO.UsuarioDTO;
import com.udea.AnalisisFinanciero_back.service.RolService;
import com.udea.AnalisisFinanciero_back.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Administración", description = "API para funciones administrativas - Solo para administradores")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    @Operation(summary = "Crear nuevo usuario", 
               description = "Permite a un administrador crear un nuevo usuario con rol específico (solo ORDENADOR_GASTO o DILIGENCIADOR)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores")
    })
    @PostMapping("/usuarios")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioDTO> crearUsuario(@Valid @RequestBody CreateUsuarioDTO createUsuarioDTO) {
        UsuarioDTO usuarioCreado = usuarioService.crearUsuarioPorAdmin(createUsuarioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
    }

    @Operation(summary = "Listar roles disponibles", 
               description = "Obtiene la lista de roles disponibles para asignar a nuevos usuarios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores")
    })
    @GetMapping("/roles")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<RolDTO>> listarRoles() {
        List<RolDTO> roles = rolService.getAllRoles();
        
        // Filtrar solo los roles que se pueden asignar
        List<RolDTO> rolesFiltrados = roles.stream()
                .filter(rol -> rol.getNombreRol().equals("ORDENADOR_GASTO") || 
                              rol.getNombreRol().equals("DILIGENCIADOR"))
                .toList();
        
        return ResponseEntity.ok(rolesFiltrados);
    }

    @Operation(summary = "Listar todos los usuarios", 
               description = "Obtiene la lista completa de usuarios del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores")
    })
    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.getAllUsers();
        return ResponseEntity.ok(usuarios);
    }
}