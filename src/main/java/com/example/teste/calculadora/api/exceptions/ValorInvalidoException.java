package com.example.teste.calculadora.api.exceptions;

/**
 * Exceção lançada quando há problemas com os valores monetários fornecidos para o cálculo
 */
public class ValorInvalidoException extends RuntimeException {
    
    public ValorInvalidoException(String message) {
        super(message);
    }
    
    public ValorInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
