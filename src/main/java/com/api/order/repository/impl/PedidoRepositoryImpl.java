package com.api.order.repository.impl;
import com.api.order.dto.PedidoDto;
import com.api.order.dto.PedidoFiltroDto;
import com.api.order.dto.PedidoProdutoDto;
import com.api.order.dto.ProdutoDto;
import com.api.order.mapper.PedidoProdutoMapper;
import com.api.order.model.Pedido;
import com.api.order.repository.PedidoRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.Query;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PedidoRepositoryImpl implements PedidoRepositoryCustom {

    @Autowired
    private  PedidoProdutoMapper pedidoProdutoMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PedidoDto> findPedidosByFilter(PedidoFiltroDto pedidoFiltroDto) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "SELECT p.CD_PEDIDO as cdPedido, " +
                "       p.NM_COMPRADOR as nmComprador, " +
                "       p.DT_PEDIDO as dtPedido, " +
                "       p.STATUS_PEDIDO as statusPedido, " +
                "       SUM(pr.VL_PRODUTO * pp.QT_PRODUTO) OVER (PARTITION BY p.CD_PEDIDO) AS vlTotal, " +
                "       pr.CD_PRODUTO as cdProduto, " +
                "       pr.NM_PRODUTO as nmProduto, " +
                "       pr.VL_PRODUTO as vlProduto, " +
                "       pp.QT_PRODUTO as qtProduto " +
                "FROM PEDIDO p " +
                "LEFT JOIN PEDIDO_PRODUTO pp ON pp.CD_PEDIDO = p.CD_PEDIDO " +
                "LEFT JOIN PRODUTO pr ON pr.CD_PRODUTO = pp.CD_PRODUTO " +
                "WHERE 1 = 1 "
        );

        if (pedidoFiltroDto.getCdProduto() != null) {
            sb.append("AND pr.CD_PRODUTO = :cdProduto ");
        }
        if (pedidoFiltroDto.getStatus() != null && !pedidoFiltroDto.getStatus().isEmpty()) {
            sb.append("AND p.STATUS_PEDIDO = :status ");
        }
        if (pedidoFiltroDto.getDtInicial() != null) {
            sb.append("AND p.DT_CRIACAO >= :dtInicial ");
        }
        if (pedidoFiltroDto.getDtFinal() != null) {
            sb.append("AND p.DT_CRIACAO <= :dtFinal ");
        }
        if (pedidoFiltroDto.getNmProduto() != null && !pedidoFiltroDto.getNmProduto().isEmpty()) {
            sb.append("AND pr.NM_PRODUTO LIKE :nmProduto ");
        }

        Query query = entityManager.createNativeQuery(sb.toString());

        if (pedidoFiltroDto.getCdProduto() != null) {
            query.setParameter("cdProduto", pedidoFiltroDto.getCdProduto());
        }
        if (pedidoFiltroDto.getStatus() != null && !pedidoFiltroDto.getStatus().isEmpty()) {
            query.setParameter("status", pedidoFiltroDto.getStatus());
        }
        if (pedidoFiltroDto.getDtInicial() != null) {
            query.setParameter("dtInicial", pedidoFiltroDto.getDtInicial());
        }
        if (pedidoFiltroDto.getDtFinal() != null) {
            query.setParameter("dtFinal", pedidoFiltroDto.getDtFinal());
        }
        if (pedidoFiltroDto.getNmProduto() != null && !pedidoFiltroDto.getNmProduto().isEmpty()) {
            query.setParameter("nmProduto", "%" + pedidoFiltroDto.getNmProduto() + "%");
        }


        return (List<PedidoDto>) mapearResultadoParaPedidoDto(query.getResultList());
    }

    public List<PedidoDto> mapearResultadoParaPedidoDto(List<Object[]> resultados) {
        Map<Long, PedidoDto> pedidosMap = new HashMap<>();

        for (Object[] linha : resultados) {

            Long cdPedido = ((Number) linha[0]).longValue(); // cdPedido
            String nmComprador = (String) linha[1]; // nmComprador
            LocalDateTime dtPedido = ((Timestamp) linha[2]).toLocalDateTime(); // dtPedido
            String statusPedido = (String) linha[3]; // statusPedido
            BigDecimal vlTotal = (BigDecimal) linha[4]; // vlTotal
            Long cdProduto = linha[5] != null ? ((Number) linha[5]).longValue() : null; // cdProduto
            String nmProduto = (String) linha[6]; // nmProduto
            BigDecimal vlProduto = (BigDecimal) linha[7]; // vlProduto
            Long qtProduto = (Long) linha[8]; // qtProduto

            // Obter ou criar o PedidoDto
            PedidoDto pedidoDto = pedidosMap.get(cdPedido);
            if (pedidoDto == null) {
                pedidoDto = new PedidoDto();
                pedidoDto.setCdPedido(cdPedido);
                pedidoDto.setNmComprador(nmComprador);
                pedidoDto.setDtPedido(dtPedido);
                pedidoDto.setStatusPedido(statusPedido);
                pedidoDto.setVlTotalPedido(vlTotal);
                pedidoDto.setItens(new ArrayList<>());
                pedidosMap.put(cdPedido, pedidoDto);
            }

            // Adicionar ProdutoDto à lista de itens
            if (cdProduto != null) {
                ProdutoDto produtoDto = new ProdutoDto();
                produtoDto.setCdProduto(cdProduto);
                produtoDto.setNmProduto(nmProduto);
                produtoDto.setVlProduto(vlProduto);

                PedidoProdutoDto itemDto = new PedidoProdutoDto();
                itemDto.setProduto(produtoDto);
                itemDto.setPedido(pedidoDto);
                itemDto.setQuantidade(qtProduto);
                pedidoDto.getItens().add(pedidoProdutoMapper.toEntity(itemDto));
            }
        }

        return new ArrayList<> (pedidosMap.values());
    }
}
