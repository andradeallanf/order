package com.example.teste.calculadora.api.exceptions;

/**
 * Exceção lançada quando há problemas com as datas fornecidas para o cálculo
 */
public class DataInvalidaException extends RuntimeException {
    
    public DataInvalidaException(String message) {
        super(message);
    }
    
    public DataInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
}
