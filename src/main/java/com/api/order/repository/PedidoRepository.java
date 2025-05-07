package com.api.order.repository;

import com.api.order.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>, PedidoRepositoryCustom {
    boolean existsByCdPedidoExterno(Long cdPedidoExterno);
}
