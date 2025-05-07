package com.example.teste.calculadora.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO que representa os dados de cálculo de juros e amortização
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude
@Schema(description = "Dados de cálculo de juros e amortização de empréstimo")
public class CalculadoraDto {

    @Schema(description = "Data de competência do cálculo", example = "2023-01-15", type = "string", format = "date")
    private LocalDate dataCompetencia;

    @Schema(description = "Valor do empréstimo", example = "100000.0")
    private Double valorEmprestimo;

    @Schema(description = "Saldo devedor atual", example = "95000.0")
    private Double saldoDevedor;

    @Schema(description = "Informação de consolidação da parcela (ex: '1/120')", example = "1/120")
    private String consolidada = "";

    @Schema(description = "Valor da parcela", example = "1200.0")
    private Double parcela;

    @Schema(description = "Valor total", example = "120000.0")
    private Double total;

    @Schema(description = "Valor da amortização", example = "833.33")
    private Double amortizacao;

    @Schema(description = "Saldo remanescente", example = "95000.0")
    private Double saldo;

    @Schema(description = "Valor da provisão de juros", example = "366.67")
    private Double provisao;

    @Schema(description = "Juros acumulados", example = "366.67")
    private Double acumulado;

    @Schema(description = "Valor pago de juros", example = "366.67")
    private Double pago;
}
