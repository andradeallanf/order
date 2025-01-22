package com.api.order.service;

import com.api.order.dto.ProdutoDto;

import java.util.List;

public interface ProdutoService {
    ProdutoDto criarProduto(ProdutoDto produtoDto);

    ProdutoDto atualizarProduto(ProdutoDto produtoDto);

    ProdutoDto findProdutoById(Long cdProduto);

    List<ProdutoDto> findAll();

    void deleteProduto(Long id);

}
