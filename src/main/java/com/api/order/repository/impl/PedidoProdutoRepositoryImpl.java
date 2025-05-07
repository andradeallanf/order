package com.api.order.repository.impl;

import com.api.order.repository.PedidoProdutoRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class PedidoProdutoRepositoryImpl implements PedidoProdutoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
}
