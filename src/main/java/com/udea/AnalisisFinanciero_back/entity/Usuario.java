package com.udea.AnalisisFinanciero_back.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "nombre", length = 80, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "documento", length = 15, nullable = false)
    private String documento;

    @Column(name = "password", length = 80, nullable = false)
    private String password;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "ultimo_acceso")
    private LocalDate ultimoAcceso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    // equals y hashCode solo por usuarioId
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return usuarioId != null && usuarioId.equals(usuario.usuarioId);
    }

    @Override
    public int hashCode() {
        return usuarioId != null ? usuarioId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "usuarioId=" + usuarioId +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", documento='" + documento + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}