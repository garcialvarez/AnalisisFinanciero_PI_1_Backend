
package com.udea.AnalisisFinanciero_back.controller;

import com.udea.AnalisisFinanciero_back.DTO.AuthRequest;
import com.udea.AnalisisFinanciero_back.DTO.AuthResponse;
import com.udea.AnalisisFinanciero_back.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "API para autenticación de usuarios")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión",
        description = "Valida credenciales y retorna JWT si son correctas."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas o usuario inactivo"),
        @ApiResponse(responseCode = "403", description = "Usuario suspendido")
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        
        // Determinar código de respuesta HTTP basado en el tipo de error
        if ("ERROR".equals(response.getType()) || "INACTIVE".equals(response.getType())) {
            return ResponseEntity.status(401).body(response);
        } else if ("SUSPENDED".equals(response.getType())) {
            return ResponseEntity.status(403).body(response);
        }
        
        return ResponseEntity.ok(response);
    }
}
