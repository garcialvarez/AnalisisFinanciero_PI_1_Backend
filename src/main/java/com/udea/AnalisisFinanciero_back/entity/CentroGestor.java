package com.udea.AnalisisFinanciero_back.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "centro_gestor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CentroGestor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "centro_gestor_id")
    private Integer centroGestorId;

    @Column(name = "codigo", length = 20, unique = true, nullable = false)
    private String codigo;

    @Column(name = "nombre_centro_gestor", length = 200)
    private String nombreCentroGestor;

    @OneToMany(mappedBy = "centroGestor")
    private List<ClasificadorPresupuestal> clasificadores;

    @OneToMany(mappedBy = "centroGestor")
    private List<DetallePonderadoCentroGestor> detallesPonderados;

}
