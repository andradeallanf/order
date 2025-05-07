package com.example.teste.calculadora.api.controller;

import com.example.teste.calculadora.api.dto.CalculadoraDto;
import com.example.teste.calculadora.api.service.CalculadoraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/calculadora")
@Tag(name = "Calculadora de Juros", description = "API para cálculo de juros e amortização de empréstimos")
public class CalculadoraController {

    @Autowired
    private CalculadoraService calculadoraService;

    @Operation(
            summary = "Calcular juros e amortização de empréstimo",
            description = "Calcula os juros e a amortização de um empréstimo com base nos parâmetros fornecidos. " +
                    "Retorna uma lista de cálculos para cada período entre a data inicial e final."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cálculo realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = List.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parâmetros inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor",
                    content = @Content
            )
    })
    @GetMapping("/calcular")
    public ResponseEntity<Iterable<CalculadoraDto>> calcular(
            @Parameter(description = "Data de início do cálculo no formato dd/MM/yyyy", required = true, example = "01/01/2023")
            @RequestParam String dataInicio,

            @Parameter(description = "Data final do cálculo no formato dd/MM/yyyy", required = true, example = "31/12/2023")
            @RequestParam String dataFim,

            @Parameter(description = "Data do primeiro pagamento no formato dd/MM/yyyy", required = true, example = "15/01/2023")
            @RequestParam String primeiroPagamento,

            @Parameter(description = "Valor do empréstimo", required = true, example = "100000")
            @RequestParam BigDecimal valorEmprestimo,

            @Parameter(description = "Taxa de juros anual em percentual", required = true, example = "12.5")
            @RequestParam double taxaJuros) {
        return ResponseEntity.ok(calculadoraService.calcularJuros(dataInicio, dataFim, primeiroPagamento, valorEmprestimo, taxaJuros));
    }
}
