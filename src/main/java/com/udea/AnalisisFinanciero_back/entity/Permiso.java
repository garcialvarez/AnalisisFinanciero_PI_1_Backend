package com.udea.AnalisisFinanciero_back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permisos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "roles") // Excluir 'roles' de toString para evitar recursividad
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permiso_id")
    private Long permisoId;

    @Column(name = "nombre_permiso", length = 50, nullable = false, unique = true)
    private String nombrePermiso;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @ManyToMany(mappedBy = "permisos")
    @JsonIgnore // Ignorar en la serialización para evitar ciclos
    private Set<Rol> roles = new HashSet<>();

    // Métodos equals y hashCode personalizados para evitar recursividad
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permiso)) return false;

        Permiso permiso = (Permiso) o;
        return permisoId != null && permisoId.equals(permiso.permisoId);
    }

    @Override
    public int hashCode() {
        return permisoId != null ? permisoId.hashCode() : 0;
    }
}

