package com.udea.AnalisisFinanciero_back.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "estado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer idEstado;

    @Column(name = "nombre_estado", length = 20, nullable = false)
    private String nombreEstado;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estado estado = (Estado) o;
        return idEstado != null && idEstado.equals(estado.idEstado);
    }

    @Override
    public int hashCode() {
        return idEstado != null ? idEstado.hashCode() : 0;
    }
}