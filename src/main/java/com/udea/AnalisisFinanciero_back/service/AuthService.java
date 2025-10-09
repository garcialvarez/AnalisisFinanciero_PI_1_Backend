package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.DTO.AuthRequest;
import com.udea.AnalisisFinanciero_back.DTO.AuthResponse;
import com.udea.AnalisisFinanciero_back.entity.Permiso;
import com.udea.AnalisisFinanciero_back.entity.Usuario;
import com.udea.AnalisisFinanciero_back.repository.UsuarioRepository;
import com.udea.AnalisisFinanciero_back.security.JwtTokenProvider;
import com.udea.AnalisisFinanciero_back.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UsuarioRepository usuarioRepository;

    /**
     * Autentica un usuario y retorna el token JWT con información del usuario
     * @param loginRequest Credenciales del usuario
     * @return AuthResponse con token y datos del usuario
     */
    public AuthResponse login(AuthRequest loginRequest) {
        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElse(null);
        
        if (usuario == null) {
            return new AuthResponse("Credenciales inválidas", "ERROR");
        }

        // Validar estado del usuario
        AuthResponse estadoValidation = validarEstadoUsuario(usuario);
        if (estadoValidation != null) {
            return estadoValidation;
        }

        try {
            // Autenticar credenciales
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Generar token JWT
            String jwt = jwtTokenProvider.generateJwtToken(authentication);

            // Actualizar último acceso
            usuario.setUltimoAcceso(LocalDate.now());
            usuarioRepository.save(usuario);

            // Preparar información del usuario
            String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombreRol() : "SIN_ROL";
            List<String> permisos = usuario.getRol() != null ?
                usuario.getRol().getPermisos().stream()
                    .map(Permiso::getNombrePermiso)
                    .collect(Collectors.toList()) : List.of();

            return new AuthResponse(
                jwt,
                userDetails.getId(),
                userDetails.getNombre(),
                userDetails.getApellido(),
                userDetails.getEmail(),
                rolNombre,
                permisos
            );

        } catch (Exception ex) {
            return new AuthResponse("Credenciales inválidas", "ERROR");
        }
    }

    /**
     * Valida el estado del usuario
     * @param usuario Usuario a validar
     * @return AuthResponse con error si el estado no es válido, null si es válido
     */
    private AuthResponse validarEstadoUsuario(Usuario usuario) {
        // Validar estado del usuario
        if (usuario.getEstado() == null || usuario.getEstado().getIdEstado() == null) {
            return new AuthResponse("El usuario no tiene estado definido", "ERROR");
        }

        if (usuario.getEstado().getIdEstado() == 3) { // 3 = SUSPENDIDO
            return new AuthResponse("El usuario está suspendido. Contacte al administrador.", "SUSPENDED");
        }

        if (usuario.getEstado().getIdEstado() != 1) { // 1 = ACTIVO
            return new AuthResponse("El usuario no está activo", "INACTIVE");
        }

        return null; // Estado válido
    }
}