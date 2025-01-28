package com.api.order.service;

import com.api.order.dto.ProdutoDto;
import com.api.order.mapper.ProdutoMapper;
import com.api.order.model.Produto;
import com.api.order.repository.ProdutoRepository;
import com.api.order.service.impl.ProdutoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceImplTest {

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    @Mock
    private ProdutoMapper produtoMapper;

    @Mock
    private ProdutoRepository produtoRepository;

    private Produto produto;
    private ProdutoDto produtoDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        produto = new Produto();
        produto.setCdProduto(1L);
        produto.setNmProduto("Produto Teste");
        produto.setVlProduto(BigDecimal.valueOf(100.0));
        produto.setDtCadastro(LocalDateTime.now());

        produtoDto = new ProdutoDto();
        produtoDto.setCdProduto(1L);
        produtoDto.setNmProduto("Produto Teste");
        produtoDto.setVlProduto(BigDecimal.valueOf(100.0));
    }

    @Test
    void testCriarProduto() {
        when(produtoMapper.toEntity(produtoDto)).thenReturn(produto);
        when(produtoRepository.save(produto)).thenReturn(produto);
        when(produtoMapper.toDto(produto)).thenReturn(produtoDto);

        ProdutoDto result = produtoService.criarProduto(produtoDto);

        assertNotNull(result);
        assertEquals(produtoDto.getCdProduto(), result.getCdProduto());
        assertEquals(produtoDto.getNmProduto(), result.getNmProduto());
        verify(produtoRepository).save(produto);
    }

    @Test
    void testAtualizarProduto() {
        when(produtoMapper.toEntity(produtoDto)).thenReturn(produto);
        when(produtoRepository.save(produto)).thenReturn(produto);
        when(produtoMapper.toDto(produto)).thenReturn(produtoDto);

        ProdutoDto result = produtoService.atualizarProduto(produtoDto);

        assertNotNull(result);
        assertEquals(produtoDto.getCdProduto(), result.getCdProduto());
        assertEquals(produtoDto.getNmProduto(), result.getNmProduto());
        verify(produtoRepository).save(produto);
    }

    @Test
    void testFindProdutoById_Success() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoMapper.toDto(produto)).thenReturn(produtoDto);

        ProdutoDto result = produtoService.findProdutoById(1L);

        assertNotNull(result);
        assertEquals(produtoDto.getCdProduto(), result.getCdProduto());
        verify(produtoRepository).findById(1L);
    }

    @Test
    void testFindProdutoById_NotFound() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> produtoService.findProdutoById(1L));
        assertEquals("Produto não encontrado com o ID: 1", exception.getMessage());

        verify(produtoRepository).findById(1L);
    }

    @Test
    void testFindAll() {
        List<Produto> produtos = Arrays.asList(produto);
        when(produtoRepository.findAll()).thenReturn(produtos);
        when(produtoMapper.toDtoList(produtos)).thenReturn(Arrays.asList(produtoDto));

        List<ProdutoDto> result = produtoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(produtoRepository).findAll();
    }

    @Test
    void testDeleteProduto() {
        doNothing().when(produtoRepository).deleteById(1L);

        produtoService.deleteProduto(1L);

        verify(produtoRepository).deleteById(1L);
    }
}
