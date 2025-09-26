
package com.udea.AnalisisFinanciero_back.controller;

import com.udea.AnalisisFinanciero_back.DTO.AuthRequest;
import com.udea.AnalisisFinanciero_back.DTO.AuthResponse;
import com.udea.AnalisisFinanciero_back.entity.Usuario;
import com.udea.AnalisisFinanciero_back.entity.Permiso;
import com.udea.AnalisisFinanciero_back.repository.UsuarioRepository;
import com.udea.AnalisisFinanciero_back.security.JwtTokenProvider;
import com.udea.AnalisisFinanciero_back.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@Tag(name = "Autenticación", description = "API para autenticación de usuarios (sin 2FA ni registro de acceso)")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión",
        description = "Valida credenciales y retorna JWT si son correctas. No requiere doble factor ni registra accesos.",
        tags = {"Autenticación"}
    )
    public ResponseEntity<?> login(@RequestBody AuthRequest loginRequest) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(401).body(new AuthResponse("Credenciales inválidas", "ERROR"));
        }
        // Validar estado del usuario
        if (usuario.getEstado() == null || usuario.getEstado().getIdEstado() == null) {
            return ResponseEntity.status(401).body(new AuthResponse("El usuario no tiene estado definido", "ERROR"));
        }
        if (usuario.getEstado().getIdEstado() == 3) { // 3 = SUSPENDIDO
            return ResponseEntity.status(403).body(new AuthResponse("El usuario está suspendido. Contacte al administrador.", "SUSPENDED"));
        }
        if (usuario.getEstado().getIdEstado() != 1) { // 1 = ACTIVO
            return ResponseEntity.status(401).body(new AuthResponse("El usuario no está activo", "INACTIVE"));
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwt = jwtTokenProvider.generateJwtToken(authentication);
            usuario.setUltimoAcceso(LocalDate.now());
            usuarioRepository.save(usuario);
            String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombreRol() : "SIN_ROL";
            List<String> permisos = usuario.getRol() != null ?
                usuario.getRol().getPermisos().stream().map(Permiso::getNombrePermiso).collect(Collectors.toList()) : List.of();
            return ResponseEntity.ok(new AuthResponse(
                jwt,
                userDetails.getId(),
                userDetails.getNombre(),
                userDetails.getApellido(),
                userDetails.getEmail(),
                rolNombre,
                permisos
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(401).body(new AuthResponse("Credenciales inválidas", "ERROR"));
        }
    }
}
