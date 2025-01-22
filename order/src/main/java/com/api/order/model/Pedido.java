package com.api.order.model;

import com.api.order.dto.PedidoDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="PEDIDO")
@Data
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "CD_PEDIDO")
    private Long cdPedido;

    @Column(name = "CD_PEDIDO_EXTERNO")
    private Long cdPedidoExterno;

    @Column(name = "NM_COMPRADOR")
    private String nmComprador;

    @Column(name = "DT_PEDIDO")
    private LocalDateTime dtPedido;

    @Column(name = "STATUS_PEDIDO")
    private String statusPedido;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PedidoProduto> itens = new ArrayList<>();

}
