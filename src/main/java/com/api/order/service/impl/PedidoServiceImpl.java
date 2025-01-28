package com.api.order.service.impl;

import com.api.order.dto.PedidoDto;
import com.api.order.dto.PedidoFiltroDto;
import com.api.order.dto.ProdutoDto;
import com.api.order.exceptions.PedidoDuplicadoException;
import com.api.order.exceptions.PedidoNotFoundException;
import com.api.order.exceptions.ProdutoNotFoundException;
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

            pedido.getItens().forEach(item -> {
                Produto produto = produtoRepository.findById(item.getProduto().getCdProduto())
                        .orElseThrow(() -> new ProdutoNotFoundException(
                                "Produto com ID " + item.getProduto().getCdProduto() + " não encontrado."));
                item.setProduto(produto);
            });

            pedido.setVlTotalPedido(calculaValorTotalPedido(pedido.getItens()));
        }

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        return pedidoMapper.toDto(pedidoSalvo);
    }

    @Override
    public PedidoDto findPedidoById(Long cdPedido) {
        Pedido pedido = pedidoRepository.findById(cdPedido)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido não encontrado com o ID: " + cdPedido));
        return pedidoMapper.toDto(pedido);
    }

    @Override
    public List<PedidoDto> findPedidosByFilter(PedidoFiltroDto pedidoFiltroDto) {
        return pedidoRepository.findPedidosByFilter(pedidoFiltroDto);
    }

    public void validaPedidoDuplicado(Long cdPedidoExterno) {
        boolean pedidoRealizado = pedidoRepository.existsByCdPedidoExterno(cdPedidoExterno);
        if (pedidoRealizado) {
            throw new PedidoDuplicadoException("Pedido com ID externo " + cdPedidoExterno + " já foi realizado.");
        }
    }

    public BigDecimal calculaValorTotalPedido(List<PedidoProduto> pedidoProdutoList) {
        return pedidoProdutoList.stream()
                .filter(item -> item.getProduto() != null
                        && item.getProduto().getVlProduto() != null
                        && item.getQuantidade() != null)
                .map(item -> item.getProduto().getVlProduto().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
