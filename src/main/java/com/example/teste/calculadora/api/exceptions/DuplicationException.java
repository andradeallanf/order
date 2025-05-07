package com.example.teste.calculadora.api.exceptions;

public class DuplicationException extends RuntimeException {

    public DuplicationException(String message) {
        super(message);
    }

    public DuplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
