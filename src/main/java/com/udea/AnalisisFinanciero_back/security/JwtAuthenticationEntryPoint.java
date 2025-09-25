package com.udea.AnalisisFinanciero_back.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que maneja las excepciones para solicitudes no autenticadas
 * y devuelve un mensaje de error 401 en formato JSON.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", HttpStatus.UNAUTHORIZED.value());
        errorDetails.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        errorDetails.put("message", "No está autorizado para acceder a este recurso. Por favor inicie sesión.");
        errorDetails.put("details", authException.getMessage());
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("timestamp", LocalDateTime.now().toString());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorDetails);
    }
}
