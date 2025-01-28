package com.api.order.service.impl;

import com.api.order.dto.ProdutoDto;
import com.api.order.mapper.ProdutoMapper;
import com.api.order.model.Produto;
import com.api.order.repository.ProdutoRepository;
import com.api.order.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    private ProdutoMapper produtoMapper;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Override
    public ProdutoDto criarProduto(ProdutoDto produtoDto) {
        if (produtoDto.getNmProduto() == null) {
            throw new RuntimeException("Nome do produto é obrigatório");
        }
        Produto produto = produtoMapper.toEntity(produtoDto);
        produto.setDtCadastro(LocalDateTime.now());
        Produto produtoSalvo = produtoRepository.save(produto);
        return produtoMapper.toDto(produtoSalvo);
    }

    @Override
    public ProdutoDto atualizarProduto(ProdutoDto produtoDto) {
        Produto produto = produtoMapper.toEntity(produtoDto);
        produto.setDtCadastro(LocalDateTime.now());
        Produto produtoSalvo = produtoRepository.save(produto);
        return produtoMapper.toDto(produtoSalvo);
    }

    @Override
    public ProdutoDto findProdutoById(Long cdProduto) {
        Produto produto = produtoRepository.findById(cdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + cdProduto));;;
        return produtoMapper.toDto(produto);
    }

    @Override
    public List<ProdutoDto> findAll() {
        List<Produto> produtoList = produtoRepository.findAll();
        return produtoMapper.toDtoList(produtoList);
    }

    @Override
    public void deleteProduto(Long id) {
        produtoRepository.deleteById(id);
    }

}
