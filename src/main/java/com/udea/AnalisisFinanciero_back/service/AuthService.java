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
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElse(null);
        
        if (usuario == null) {
            return AuthResponse.error("Credenciales inválidas");
        }

        AuthResponse estadoValidation = validarEstadoUsuario(usuario);
        if (estadoValidation != null) {
            return estadoValidation;
        }

        try {
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

            String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombreRol() : "SIN_ROL";
            List<String> permisos = usuario.getRol() != null ?
                usuario.getRol().getPermisos().stream()
                    .map(Permiso::getNombrePermiso)
                    .collect(Collectors.toList()) : List.of();

            return AuthResponse.loginSuccess(
                jwt,
                userDetails.getId(),
                userDetails.getNombre(),
                userDetails.getApellido(),
                userDetails.getEmail(),
                rolNombre,
                permisos
            );

        } catch (Exception ex) {
            return AuthResponse.error("Credenciales inválidas");
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
            return AuthResponse.error("El usuario no tiene estado definido");
        }

        if (usuario.getEstado().getIdEstado() == 3) { // 3 = SUSPENDIDO
            return AuthResponse.message("El usuario está suspendido. Contacte al administrador.", "SUSPENDED");
        }

        if (usuario.getEstado().getIdEstado() != 1) { // 1 = ACTIVO
            return AuthResponse.message("El usuario no está activo", "INACTIVE");
        }

        return null;
    }
}