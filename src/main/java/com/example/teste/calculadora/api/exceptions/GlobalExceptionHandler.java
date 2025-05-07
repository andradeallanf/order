package com.example.teste.calculadora.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicationException.class)
    public ResponseEntity<String> handleDuplicationException(DuplicationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handlePNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Tratamento para exceções de data inválida
     */
    @ExceptionHandler(DataInvalidaException.class)
    public ResponseEntity<String> handleDataInvalidaException(DataInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erro de data: " + ex.getMessage());
    }

    /**
     * Tratamento para exceções de valor inválido
     */
    @ExceptionHandler(ValorInvalidoException.class)
    public ResponseEntity<String> handleValorInvalidoException(ValorInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erro de valor: " + ex.getMessage());
    }

    /**
     * Tratamento para exceções de taxa de juros inválida
     */
    @ExceptionHandler(TaxaJurosInvalidaException.class)
    public ResponseEntity<String> handleTaxaJurosInvalidaException(TaxaJurosInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Erro de taxa de juros: " + ex.getMessage());
    }

    /**
     * Tratamento para exceções de cálculo
     */
    @ExceptionHandler(CalculoException.class)
    public ResponseEntity<String> handleCalculoException(CalculoException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro de cálculo: " + ex.getMessage());
    }

    /**
     * Tratamento para exceções de formato de data
     */
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Formato de data inválido: " + ex.getMessage());
    }

    /**
     * Tratamento para exceções de tipo de argumento
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Tipo de parâmetro inválido: " + ex.getMessage());
    }

    /**
     * Tratamento para exceções gerais
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno: " + ex.getMessage());
    }
}
