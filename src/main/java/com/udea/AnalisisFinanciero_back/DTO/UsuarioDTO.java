package com.udea.AnalisisFinanciero_back.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long usuarioId; // No incluir en el JSON de creación
    private String nombre;
    private String apellido;
    private String email;
    private String documento;
    private String password;
    private String telefono;
    private LocalDate fechaRegistro;
    private LocalDate ultimoAcceso;
    private Integer estado; // Solo el ID
    private Integer rolId; // Solo el ID del rol
}
