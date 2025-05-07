package com.api.order.service;

import com.api.order.dto.PedidoDto;
import com.api.order.dto.PedidoFiltroDto;

import java.util.List;

public interface PedidoService {

    PedidoDto criarPedido(PedidoDto pedidoDto);

    PedidoDto findPedidoById(Long cdPedido);

    List<PedidoDto> findPedidosByFilter(PedidoFiltroDto pedidoFiltroDto);
}
