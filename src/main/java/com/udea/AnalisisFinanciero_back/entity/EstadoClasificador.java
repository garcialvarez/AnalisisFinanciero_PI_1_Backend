package com.udea.AnalisisFinanciero_back.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "estado_clasificador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoClasificador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer idEstado;

    @Column(name = "nombre_estado", length = 50, nullable = false)
    private String nombreEstado;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @OneToMany(mappedBy = "estadoClasificador")
    @JsonManagedReference("estado-clasificadores")
    private List<ClasificadorPresupuestal> clasificadores;

}
