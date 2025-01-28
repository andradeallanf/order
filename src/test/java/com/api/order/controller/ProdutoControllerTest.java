package com.api.order.controller;

import com.api.order.dto.ProdutoDto;
import com.api.order.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    private ProdutoDto produtoDto;

    @BeforeEach
    void setUp() {
        produtoDto = new ProdutoDto();
        produtoDto.setCdProduto(1L);
        produtoDto.setNmProduto("Produto Teste");
        produtoDto.setVlProduto(BigDecimal.valueOf(100.0));
    }

    @Test
    void criarProduto_ValidProdutoDto_ReturnsOk() {
        when(produtoService.criarProduto(any(ProdutoDto.class))).thenReturn(produtoDto);

        ResponseEntity<ProdutoDto> response = produtoController.criarProduto(produtoDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(produtoDto.getCdProduto(), response.getBody().getCdProduto());
        verify(produtoService, times(1)).criarProduto(produtoDto);
    }

    @Test
    void atualizarProduto_ValidProdutoDto_ReturnsOk() {
        when(produtoService.atualizarProduto(any(ProdutoDto.class))).thenReturn(produtoDto);

        ResponseEntity<ProdutoDto> response = produtoController.atualizarProduto(produtoDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(produtoDto.getCdProduto(), response.getBody().getCdProduto());
        verify(produtoService, times(1)).atualizarProduto(produtoDto);
    }

    @Test
    void deleteById_ValidId_ReturnsNoContent() {
        doNothing().when(produtoService).deleteProduto(anyLong());

        ResponseEntity<Void> response = produtoController.deleteById(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(produtoService, times(1)).deleteProduto(1L);
    }

    @Test
    void getProdutoById_ProdutoFound_ReturnsOk() {
        when(produtoService.findProdutoById(anyLong())).thenReturn(produtoDto);

        ResponseEntity<ProdutoDto> response = produtoController.getProdutoById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(produtoDto.getCdProduto(), response.getBody().getCdProduto());
        verify(produtoService, times(1)).findProdutoById(1L);
    }

    @Test
    void findAll_ProdutosFound_ReturnsOk() {
        when(produtoService.findAll()).thenReturn(Collections.singletonList(produtoDto));

        ResponseEntity<List<ProdutoDto>> response = produtoController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(produtoService, times(1)).findAll();
    }

    @Test
    void findAll_NoProdutosFound_ReturnsEmptyList() {
        when(produtoService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<ProdutoDto>> response = produtoController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(produtoService, times(1)).findAll();
    }
}
