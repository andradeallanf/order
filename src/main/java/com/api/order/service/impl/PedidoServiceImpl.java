package com.api.order.service.impl;

import com.api.order.dto.PedidoDto;
import com.api.order.dto.PedidoFiltroDto;
import com.api.order.dto.ProdutoDto;
import com.api.order.mapper.PedidoMapper;
import com.api.order.model.Pedido;
import com.api.order.model.PedidoProduto;
import com.api.order.model.Produto;
import com.api.order.repository.PedidoRepository;
import com.api.order.repository.ProdutoRepository;
import com.api.order.service.PedidoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoMapper pedidoMapper;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public PedidoDto criarPedido(PedidoDto pedidoDto) {
        validaPedidoDuplicado(pedidoDto.getCdPedidoExterno());
        Pedido pedido = pedidoMapper.toEntity(pedidoDto);
        pedido.setDtPedido(LocalDateTime.now());

        if (pedido.getItens() != null) {
            pedido.getItens().forEach(item -> item.setPedido(pedido));
        }
        Pedido pedidoSalvo =  pedidoRepository.save(pedido);
        pedidoSalvo.getItens().forEach(item -> {
            Produto produto = produtoRepository.findById(item.getProduto().getCdProduto())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Produto com ID " + item.getProduto().getCdProduto() + " não encontrado."));
            item.setProduto(produto);
        });
        return pedidoMapper.toDto(pedidoSalvo);
    }

    @Override
    public PedidoDto findPedidoById(Long cdPedido) {
        Pedido pedido = pedidoRepository.findById(cdPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com o ID: " + cdPedido));;
        return pedidoMapper.toDto(pedido);
    }

    @Override
    public List<PedidoDto> findPedidosByFilter(PedidoFiltroDto pedidoFiltroDto) {
        return pedidoRepository.findPedidosByFilter(pedidoFiltroDto);
    }

    public void validaPedidoDuplicado(Long cdPedidoExterno) {
        boolean pedidoRealizado = pedidoRepository.existsByCdPedidoExterno(cdPedidoExterno);
        if (pedidoRealizado) {
            throw  new RuntimeException("Pedido já realizado ");
        }
    }


}
