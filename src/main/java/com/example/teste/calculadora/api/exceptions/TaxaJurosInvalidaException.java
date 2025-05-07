package com.example.teste.calculadora.api.exceptions;

/**
 * Exceção lançada quando a taxa de juros fornecida é inválida
 */
public class TaxaJurosInvalidaException extends RuntimeException {
    
    public TaxaJurosInvalidaException(String message) {
        super(message);
    }
    
    public TaxaJurosInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
}
