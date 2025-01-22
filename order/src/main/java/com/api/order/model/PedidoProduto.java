package com.api.order.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="PEDIDO_PRODUTO")
@Data
@NoArgsConstructor
public class PedidoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CD_PEDIDO")
    @JsonBackReference
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CD_PRODUTO")
    private Produto produto;

    @Column(name = "QT_PRODUTO")
    private Long quantidade;
}
