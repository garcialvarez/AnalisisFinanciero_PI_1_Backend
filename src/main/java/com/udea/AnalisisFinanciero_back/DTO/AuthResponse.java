package com.udea.AnalisisFinanciero_back.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String rolNombre;
    private List<String> permisos;
    private String message;
    private String status;

    // Constructor completo para login exitoso (con JWT)
    public AuthResponse(String token, Long id, String nombre, String apellido,
                        String email, String rolNombre, List<String> permisos) {
        this.token = token;
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rolNombre = rolNombre;
        this.permisos = permisos;
        this.status = "LOGIN_SUCCESS";
    }

    // Constructor para mensajes del primer paso (sin JWT)
    public AuthResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }

    public List<String> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<String> permisos) {
        this.permisos = permisos;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
