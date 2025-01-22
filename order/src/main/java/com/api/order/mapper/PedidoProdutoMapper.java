package com.api.order.mapper;

import com.api.order.dto.PedidoProdutoDto;
import com.api.order.model.PedidoProduto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PedidoProdutoMapper {

    PedidoProdutoDto toDto(PedidoProduto pedidoProduto);

    PedidoProduto toEntity(PedidoProdutoDto pedidoProdutoDto);
}
