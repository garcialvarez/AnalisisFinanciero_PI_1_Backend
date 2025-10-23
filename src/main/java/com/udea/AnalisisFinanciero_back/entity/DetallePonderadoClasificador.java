package com.udea.AnalisisFinanciero_back.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.udea.AnalisisFinanciero_back.service.DetallePonderadoEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_ponderadoxclasificador")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePonderadoClasificador implements DetallePonderadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detalle_ponderado_id")
    private Integer detallePonderadoId;

    @ManyToOne
    @JoinColumn(name = "clasificador_id")
    @JsonBackReference("clasificador-detallesPonderados")
    private ClasificadorPresupuestal clasificadorPresupuestal;

    @Column(name = "enero", precision = 5, scale = 2)
    private BigDecimal enero;
    @Column(name = "febrero", precision = 5, scale = 2)
    private BigDecimal febrero;
    @Column(name = "marzo", precision = 5, scale = 2)
    private BigDecimal marzo;
    @Column(name = "abril", precision = 5, scale = 2)
    private BigDecimal abril;
    @Column(name = "mayo", precision = 5, scale = 2)
    private BigDecimal mayo;
    @Column(name = "junio", precision = 5, scale = 2)
    private BigDecimal junio;
    @Column(name = "julio", precision = 5, scale = 2)
    private BigDecimal julio;
    @Column(name = "agosto", precision = 5, scale = 2)
    private BigDecimal agosto;
    @Column(name = "septiembre", precision = 5, scale = 2)
    private BigDecimal septiembre;
    @Column(name = "octubre", precision = 5, scale = 2)
    private BigDecimal octubre;
    @Column(name = "noviembre", precision = 5, scale = 2)
    private BigDecimal noviembre;
    @Column(name = "diciembre", precision = 5, scale = 2)
    private BigDecimal diciembre;
    @Column(name = "total", precision = 7, scale = 2)
    private BigDecimal total;

}
