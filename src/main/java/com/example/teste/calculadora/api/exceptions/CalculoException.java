package com.example.teste.calculadora.api.exceptions;

/**
 * Exceção lançada quando ocorre um erro durante o processo de cálculo
 */
public class CalculoException extends RuntimeException {
    
    public CalculoException(String message) {
        super(message);
    }
    
    public CalculoException(String message, Throwable cause) {
        super(message, cause);
    }
}
