package com.udea.AnalisisFinanciero_back.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private String token;
    
    @Builder.Default
    private String type = "Bearer";
    
    @Positive(message = "El ID debe ser un número positivo")
    private Long id;
    
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    
    @NotBlank(message = "El apellido no puede estar vacío") 
    private String apellido;
    
    @Email(message = "El email debe tener un formato válido")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;
    
    @NotBlank(message = "El rol no puede estar vacío")
    private String rolNombre;
    
    private List<String> permisos;
    
    private String message;
    
    private String status;
    
    
    public static AuthResponse loginSuccess(String token, Long id, String nombre, String apellido,
                                          String email, String rolNombre, List<String> permisos) {
        return AuthResponse.builder()
                .token(token)
                .id(id)
                .nombre(nombre)
                .apellido(apellido)
                .email(email)
                .rolNombre(rolNombre)
                .permisos(permisos)
                .status("LOGIN_SUCCESS")
                .build();
    }
    
    public static AuthResponse message(String message, String status) {
        return AuthResponse.builder()
                .message(message)
                .status(status)
                .build();
    }
    
    public static AuthResponse error(String message) {
        return AuthResponse.builder()
                .message(message)
                .status("ERROR")
                .build();
    }
}
