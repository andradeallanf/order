package com.api.order.repository;

import com.api.order.model.PedidoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoProdutoRepository extends JpaRepository<PedidoProduto, Long>, PedidoProdutoRepositoryCustom{
}
