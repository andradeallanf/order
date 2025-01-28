package com.api.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude
public class PedidoFiltroDto {
    private Long cdProduto;
    private Long cdPedidoExterno;
    private String status;
    private LocalDateTime dtInicial;
    private LocalDateTime dtFinal;
    private String nmProduto;
    private BigDecimal vlTotalInicial;
    private BigDecimal vlTotalFinal;
}
