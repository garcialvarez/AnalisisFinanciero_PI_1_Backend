package com.udea.AnalisisFinanciero_back.service;

import java.math.BigDecimal;

/**
 * Interfaz común para entidades de detalle ponderado.
 * Implementa el Liskov Substitution Principle (LSP) del patrón SOLID.
 */
public interface DetallePonderadoEntity {
    // Getters
    BigDecimal getEnero();
    BigDecimal getFebrero();
    BigDecimal getMarzo();
    BigDecimal getAbril();
    BigDecimal getMayo();
    BigDecimal getJunio();
    BigDecimal getJulio();
    BigDecimal getAgosto();
    BigDecimal getSeptiembre();
    BigDecimal getOctubre();
    BigDecimal getNoviembre();
    BigDecimal getDiciembre();
    BigDecimal getTotal();
    
    // Setters
    void setEnero(BigDecimal enero);
    void setFebrero(BigDecimal febrero);
    void setMarzo(BigDecimal marzo);
    void setAbril(BigDecimal abril);
    void setMayo(BigDecimal mayo);
    void setJunio(BigDecimal junio);
    void setJulio(BigDecimal julio);
    void setAgosto(BigDecimal agosto);
    void setSeptiembre(BigDecimal septiembre);
    void setOctubre(BigDecimal octubre);
    void setNoviembre(BigDecimal noviembre);
    void setDiciembre(BigDecimal diciembre);
    void setTotal(BigDecimal total);
}