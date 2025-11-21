package com.udea.AnalisisFinanciero_back.service;

import com.udea.AnalisisFinanciero_back.DTO.AuthRequest;
import com.udea.AnalisisFinanciero_back.DTO.AuthResponse;
import com.udea.AnalisisFinanciero_back.entity.Estado;
import com.udea.AnalisisFinanciero_back.entity.Permiso;
import com.udea.AnalisisFinanciero_back.entity.Rol;
import com.udea.AnalisisFinanciero_back.entity.Usuario;
import com.udea.AnalisisFinanciero_back.repository.UsuarioRepository;
import com.udea.AnalisisFinanciero_back.security.JwtTokenProvider;
import com.udea.AnalisisFinanciero_back.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@ActiveProfiles("test")
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthService authService;

    private Usuario activeUser;
    private Usuario inactiveUser;
    private Usuario suspendedUser;
    private AuthRequest validAuthRequest;
    private Authentication mockAuthentication;

    @BeforeEach
    void setUp() {
        validAuthRequest = new AuthRequest();
        validAuthRequest.setEmail("admin@analisisfinanciero.com");
        validAuthRequest.setPassword("admin123");

        // Estado activo
        Estado estadoActivo = new Estado();
        estadoActivo.setIdEstado(1);
        estadoActivo.setNombreEstado("ACTIVO");

        // Estado inactivo
        Estado estadoInactivo = new Estado();
        estadoInactivo.setIdEstado(2);
        estadoInactivo.setNombreEstado("INACTIVO");

        // Estado suspendido
        Estado estadoSuspendido = new Estado();
        estadoSuspendido.setIdEstado(3);
        estadoSuspendido.setNombreEstado("SUSPENDIDO");

        // Permisos
        Permiso permisoRead = new Permiso();
        permisoRead.setNombrePermiso("READ");
        Permiso permisoWrite = new Permiso();
        permisoWrite.setNombrePermiso("WRITE");

        // Rol
        Rol rolAdmin = new Rol();
        rolAdmin.setNombreRol("ADMIN");
        rolAdmin.setPermisos(Set.of(permisoRead, permisoWrite));

        // Usuario activo
        activeUser = new Usuario();
        activeUser.setUsuarioId(1L); // Corregido: usar setUsuarioId con Long
        activeUser.setEmail("admin@analisisfinanciero.com");
        activeUser.setNombre("Admin");
        activeUser.setApellido("User");
        activeUser.setPassword("$2a$10$encodedPassword"); // Añadir password
        activeUser.setEstado(estadoActivo);
        activeUser.setRol(rolAdmin);

        // Usuario inactivo
        inactiveUser = new Usuario();
        inactiveUser.setUsuarioId(2L); // Corregido: usar setUsuarioId con Long
        inactiveUser.setEmail("inactive@test.com");
        inactiveUser.setEstado(estadoInactivo);

        // Usuario suspendido
        suspendedUser = new Usuario();
        suspendedUser.setUsuarioId(3L); // Corregido: usar setUsuarioId con Long
        suspendedUser.setEmail("suspended@test.com");
        suspendedUser.setEstado(estadoSuspendido);

        // Mock Authentication - Usar constructor correcto de UserDetailsImpl
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        authorities.add(new SimpleGrantedAuthority("READ"));
        authorities.add(new SimpleGrantedAuthority("WRITE"));
        
        UserDetailsImpl userDetails = new UserDetailsImpl(
            1L, 
            "admin@analisisfinanciero.com", 
            "$2a$10$encodedPassword", 
            "Admin", 
            "User", 
            authorities
        );
        
        mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getPrincipal()).thenReturn(userDetails);
    }

    @Test
    @DisplayName("Should return success response when login is successful")
    void testLoginSuccess() {
        // Arrange
        when(usuarioRepository.findByEmail(validAuthRequest.getEmail()))
            .thenReturn(Optional.of(activeUser));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuthentication);
        when(jwtTokenProvider.generateJwtToken(mockAuthentication))
            .thenReturn("jwt-token-test");
        when(usuarioRepository.save(any(Usuario.class)))
            .thenReturn(activeUser);

        // Act
        AuthResponse response = authService.login(validAuthRequest);

        // Assert
        assertNotNull(response);
        assertEquals("LOGIN_SUCCESS", response.getStatus());
        assertEquals("jwt-token-test", response.getToken());
        assertEquals("Admin", response.getNombre());
        assertEquals("ADMIN", response.getRolNombre());
        assertNotNull(response.getPermisos());
        assertFalse(response.getPermisos().isEmpty());

        // Verify interactions
        verify(usuarioRepository).findByEmail(validAuthRequest.getEmail());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateJwtToken(mockAuthentication);
        verify(usuarioRepository).save(activeUser);
        assertEquals(LocalDate.now(), activeUser.getUltimoAcceso());
    }

    @Test
    @DisplayName("Should return error when user not found")
    void testLoginUserNotFound() {
        // Arrange
        when(usuarioRepository.findByEmail(validAuthRequest.getEmail()))
            .thenReturn(Optional.empty());

        // Act
        AuthResponse response = authService.login(validAuthRequest);

        // Assert
        assertNotNull(response);
        assertEquals("ERROR", response.getStatus());
        assertEquals("Credenciales inválidas", response.getMessage());

        verify(usuarioRepository).findByEmail(validAuthRequest.getEmail());
        verifyNoInteractions(authenticationManager);
    }

    @Test
    @DisplayName("Should return inactive response when user is inactive")
    void testLoginUserInactive() {
        // Arrange
        when(usuarioRepository.findByEmail(validAuthRequest.getEmail()))
            .thenReturn(Optional.of(inactiveUser));

        // Act
        AuthResponse response = authService.login(validAuthRequest);

        // Assert
        assertNotNull(response);
        assertEquals("INACTIVE", response.getStatus());
        assertEquals("El usuario no está activo", response.getMessage());
        assertNull(response.getToken());

        verify(usuarioRepository).findByEmail(validAuthRequest.getEmail());
        verifyNoInteractions(authenticationManager);
    }

    @Test
    @DisplayName("Should return suspended response when user is suspended")
    void testLoginUserSuspended() {
        // Arrange
        when(usuarioRepository.findByEmail(validAuthRequest.getEmail()))
            .thenReturn(Optional.of(suspendedUser));

        // Act
        AuthResponse response = authService.login(validAuthRequest);

        // Assert
        assertNotNull(response);
        assertEquals("SUSPENDED", response.getStatus());
        assertEquals("El usuario está suspendido. Contacte al administrador.", response.getMessage());
        assertNull(response.getToken());

        verify(usuarioRepository).findByEmail(validAuthRequest.getEmail());
        verifyNoInteractions(authenticationManager);
    }

    @Test
    @DisplayName("Should return error when user has no state defined")
    void testLoginUserWithoutState() {
        // Arrange
        Usuario userWithoutState = new Usuario();
        userWithoutState.setEmail("nostate@test.com");
        userWithoutState.setEstado(null);

        when(usuarioRepository.findByEmail(validAuthRequest.getEmail()))
            .thenReturn(Optional.of(userWithoutState));

        // Act
        AuthResponse response = authService.login(validAuthRequest);

        // Assert
        assertNotNull(response);
        assertEquals("ERROR", response.getStatus());
        assertEquals("El usuario no tiene estado definido", response.getMessage());
        assertNull(response.getToken());
    }

    @Test
    @DisplayName("Should return error when authentication fails")
    void testLoginAuthenticationException() {
        // Arrange
        when(usuarioRepository.findByEmail(validAuthRequest.getEmail()))
            .thenReturn(Optional.of(activeUser));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act
        AuthResponse response = authService.login(validAuthRequest);

        // Assert
        assertNotNull(response);
        assertEquals("ERROR", response.getStatus());
        assertEquals("Credenciales inválidas", response.getMessage());

        verify(usuarioRepository).findByEmail(validAuthRequest.getEmail());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    @DisplayName("Should handle user without role gracefully")
    void testLoginUserWithoutRole() {
        // Arrange
        Usuario userWithoutRole = new Usuario();
        userWithoutRole.setUsuarioId(1L);
        userWithoutRole.setEmail("admin@analisisfinanciero.com");
        userWithoutRole.setNombre("Admin");
        userWithoutRole.setApellido("User");
        
        Estado estadoActivo = new Estado();
        estadoActivo.setIdEstado(1);
        userWithoutRole.setEstado(estadoActivo);
        userWithoutRole.setRol(null); // Sin rol

        UserDetailsImpl userDetailsWithoutRole = new UserDetailsImpl(
            1L, 
            "admin@analisisfinanciero.com", 
            "password", 
            "Admin", 
            "User", 
            new ArrayList<>()
        );
        Authentication mockAuthWithoutRole = mock(Authentication.class);
        when(mockAuthWithoutRole.getPrincipal()).thenReturn(userDetailsWithoutRole);

        when(usuarioRepository.findByEmail(validAuthRequest.getEmail()))
            .thenReturn(Optional.of(userWithoutRole));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuthWithoutRole);
        when(jwtTokenProvider.generateJwtToken(mockAuthWithoutRole))
            .thenReturn("jwt-token-test");

        // Act
        AuthResponse response = authService.login(validAuthRequest);

        // Assert
        assertNotNull(response);
        assertEquals("LOGIN_SUCCESS", response.getStatus());
        assertEquals("SIN_ROL", response.getRolNombre());
        assertTrue(response.getPermisos().isEmpty());
    }
}