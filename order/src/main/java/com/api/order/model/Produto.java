package com.api.order.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="PRODUTO")
@Data
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CD_PRODUTO")
    private Long cdProduto;

    @Column(name = "NM_PRODUTO")
    private String nmProduto;

    @Column(name = "VL_PRODUTO")
    private BigDecimal vlProduto;

    @Column(name = "DT_CADASTRO")
    private LocalDateTime dtCadastro;

}
