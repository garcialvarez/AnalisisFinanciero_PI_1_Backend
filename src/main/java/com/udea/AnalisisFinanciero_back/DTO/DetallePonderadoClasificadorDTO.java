package com.udea.AnalisisFinanciero_back.DTO;

import com.udea.AnalisisFinanciero_back.service.DetallePonderadoDTO;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class DetallePonderadoClasificadorDTO implements DetallePonderadoDTO {
    private Integer detallePonderadoId;
    private Integer clasificadorId;

    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal enero;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal febrero;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal marzo;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal abril;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal mayo;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal junio;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal julio;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal agosto;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal septiembre;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal octubre;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal noviembre;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal diciembre;
    @NotNull
    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal total;
}
