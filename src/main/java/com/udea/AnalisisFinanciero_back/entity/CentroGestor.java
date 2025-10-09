package com.udea.AnalisisFinanciero_back.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonManagedReference("centroGestor-clasificadores")
    private List<ClasificadorPresupuestal> clasificadores;

    @OneToMany(mappedBy = "centroGestor")
    @JsonManagedReference("centroGestor-detallesPonderados")
    private List<DetallePonderadoCentroGestor> detallesPonderados;

}
