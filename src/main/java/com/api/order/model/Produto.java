package com.api.order.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="PRODUTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CD_PRODUTO")
    private Long cdProduto;

    @NotNull(message = "O nome do produto é obrigatório")
    @Column(name = "NM_PRODUTO")
    private String nmProduto;

    @NotNull(message = "O valor do produto é obrigatório")
    @Column(name = "VL_PRODUTO")
    private BigDecimal vlProduto;

    @Column(name = "DT_CADASTRO")
    private LocalDateTime dtCadastro;

}
