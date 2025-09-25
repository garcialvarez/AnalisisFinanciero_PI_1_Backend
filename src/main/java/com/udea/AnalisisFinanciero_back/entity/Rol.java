package com.udea.AnalisisFinanciero_back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "permisos") // Excluir 'permisos' de toString para evitar recursividad
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Integer rolId;

    @Column(name = "nombre_rol", length = 50, nullable = false, unique = true)
    private String nombreRol;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "permisos_x_rol",
            joinColumns = @JoinColumn(name = "rol_id"),
            inverseJoinColumns = @JoinColumn(name = "permiso_id")
    )
    private Set<Permiso> permisos = new HashSet<>();

    // Métodos equals y hashCode personalizados para evitar recursividad
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rol)) return false;

        Rol rol = (Rol) o;
        return rolId != null && rolId.equals(rol.rolId);
    }

    @Override
    public int hashCode() {
        return rolId != null ? rolId.hashCode() : 0;
    }
}
