package com.api.order.repository;

import com.api.order.dto.PedidoDto;
import com.api.order.dto.PedidoFiltroDto;

import java.util.List;

public interface PedidoRepositoryCustom {

    List<PedidoDto> findPedidosByFilter(PedidoFiltroDto pedidoFiltroDto);
}
