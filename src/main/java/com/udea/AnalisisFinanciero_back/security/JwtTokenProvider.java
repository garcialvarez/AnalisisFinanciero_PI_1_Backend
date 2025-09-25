package com.udea.AnalisisFinanciero_back.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    // Tiempo de expiración en milisegundos
    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    // Generar el token JWT a partir de los datos del usuario autenticado
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        // Extraer el rol y permisos del usuario utilizando streams
        String role = userPrincipal.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .filter(auth -> auth.startsWith("ROLE_"))
                .map(auth -> auth.substring(5)) // Eliminar el prefijo "ROLE_"
                .findFirst()
                .orElse("");

        List<String> permissions = userPrincipal.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .filter(auth -> !auth.startsWith("ROLE_"))
                .collect(java.util.stream.Collectors.toList());

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("id", userPrincipal.getId())
                .claim("nombre", userPrincipal.getNombre())
                .claim("apellido", userPrincipal.getApellido())
                .claim("role", role)
                .claim("permisos", permissions)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // Extraer el nombre de usuario del token JWT
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Obtener el ID del usuario desde el JWT
    public Long getUserIdFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id", Long.class);
    }

    // Obtener el rol del usuario desde el JWT
    public String getRoleFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    // Obtener los permisos del usuario desde el JWT
    @SuppressWarnings("unchecked")
    public List<String> getPermissionsFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("permisos", List.class);
    }

    // Validar el token JWT
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("La cadena claims del JWT está vacía: {}", e.getMessage());
        }

        return false;
    }
}
