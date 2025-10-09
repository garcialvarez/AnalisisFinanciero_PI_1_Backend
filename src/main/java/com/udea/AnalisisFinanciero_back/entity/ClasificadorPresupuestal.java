package com.udea.AnalisisFinanciero_back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Table(name = "clasificador_presupuestal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClasificadorPresupuestal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clasificador_id")
    private Integer clasificadorId;

    @Column(name = "codigo", length = 20, unique = true, nullable = false)
    private String codigo;

    @Column(name = "nombre_clasificador", length = 200)
    private String nombreClasificador;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "clase_pospre", length = 80)
    private String clasePospre;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    @JsonBackReference("estado-clasificadores")
    private EstadoClasificador estadoClasificador;

    @ManyToOne
    @JoinColumn(name = "centro_gestor_id")
    @JsonBackReference("centroGestor-clasificadores")
    private CentroGestor centroGestor;

    @OneToMany(mappedBy = "clasificadorPresupuestal")
    @JsonManagedReference("clasificador-detallesPonderados")
    private List<DetallePonderadoClasificador> detallesPonderados;

}
