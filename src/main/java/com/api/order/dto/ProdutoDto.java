package com.api.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude
public class ProdutoDto {

    private Long cdProduto;

    @NotNull(message = "O nome do produto é obrigatório")
    private String nmProduto;

    @NotNull(message = "O valor do produto é obrigatório")
    private BigDecimal vlProduto;

    private LocalDate dtCadastro;
}
