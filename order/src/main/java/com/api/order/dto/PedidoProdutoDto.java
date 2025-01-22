package com.api.order.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude
public class PedidoProdutoDto {
    private Long id;
    private PedidoDto pedido;
    private ProdutoDto produto;
    private Long quantidade;
}
