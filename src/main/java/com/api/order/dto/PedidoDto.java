package com.api.order.dto;

import com.api.order.model.Pedido;
import com.api.order.model.PedidoProduto;
import com.api.order.model.Produto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude
public class PedidoDto {
    private Long cdPedido;
    private Long cdPedidoExterno;
    private LocalDateTime dtPedido;
    private String nmComprador;
    private String statusPedido;
    private BigDecimal vlTotalPedido;
    private List<PedidoProduto> itens;
}
