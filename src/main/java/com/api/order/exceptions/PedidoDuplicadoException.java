package com.api.order.exceptions;

public class PedidoDuplicadoException extends RuntimeException {

    public PedidoDuplicadoException(String message) {
        super(message);
    }

    public PedidoDuplicadoException(String message, Throwable cause) {
        super(message, cause);
    }
}
