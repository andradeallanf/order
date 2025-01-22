package com.api.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private String nmProduto;
    private BigDecimal vlProduto;
    private LocalDate dtCadastro;
}
