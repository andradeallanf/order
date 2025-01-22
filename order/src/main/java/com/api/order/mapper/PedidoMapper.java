package com.api.order.mapper;

import com.api.order.dto.PedidoDto;
import com.api.order.model.Pedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    PedidoDto toDto(Pedido pedido);

    Pedido toEntity(PedidoDto pedidoDto);
}
