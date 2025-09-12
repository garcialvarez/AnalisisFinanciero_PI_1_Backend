package com.example.AnalisisFinanciero_back.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "tipo_documento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_documento")
    private Integer idTipoDocumento;

    @Column(name = "tipo_documento", nullable = false)
    private String tipoDocumento;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoDocumento tipoDoc = (TipoDocumento) o;
        return idTipoDocumento != null && idTipoDocumento.equals(tipoDoc.idTipoDocumento);
    }

    @Override
    public int hashCode() {
        return idTipoDocumento != null ? idTipoDocumento.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TipoDocumento{" +
                "idTipoDocumento=" + idTipoDocumento +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                '}';
    }
}