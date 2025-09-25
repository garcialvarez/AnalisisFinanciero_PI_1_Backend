package com.udea.AnalisisFinanciero_back.exceptions;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(String message) {
        super(message);
    }
}
