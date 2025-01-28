package com.api.order.service;

import com.api.order.dto.PedidoDto;
import com.api.order.dto.PedidoFiltroDto;
import com.api.order.exceptions.PedidoDuplicadoException;
import com.api.order.exceptions.PedidoNotFoundException;
import com.api.order.exceptions.ProdutoNotFoundException;
import com.api.order.mapper.PedidoMapper;
import com.api.order.model.Pedido;
import com.api.order.model.PedidoProduto;
import com.api.order.model.Produto;
import com.api.order.repository.PedidoRepository;
import com.api.order.repository.ProdutoRepository;
import com.api.order.service.impl.PedidoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoMapper pedidoMapper;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarPedidoDuplicado() {
        Long cdPedidoExterno = 123L;
        PedidoDto pedidoDto = new PedidoDto();
        pedidoDto.setCdPedidoExterno(cdPedidoExterno);

        when(pedidoRepository.existsByCdPedidoExterno(cdPedidoExterno)).thenReturn(true);

        assertThrows(PedidoDuplicadoException.class, () -> pedidoService.criarPedido(pedidoDto));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testCriarPedidoProdutoNaoEncontrado() {
        Long cdProdutoNaoEncontrado = 999L;
        PedidoDto pedidoDto = new PedidoDto();
        pedidoDto.setCdPedidoExterno(123L);
        PedidoProduto pedidoProduto = new PedidoProduto();
        Produto produto= new Produto();
        produto.setCdProduto(cdProdutoNaoEncontrado);
        pedidoProduto.setProduto(produto);
        pedidoDto.setItens(List.of(pedidoProduto));

        when(pedidoRepository.existsByCdPedidoExterno(pedidoDto.getCdPedidoExterno())).thenReturn(false);
        when(produtoRepository.findById(cdProdutoNaoEncontrado)).thenReturn(Optional.empty());

        assertThrows(ProdutoNotFoundException.class, () -> pedidoService.criarPedido(pedidoDto));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testCriarPedidoSalvarPedido() {
        Long cdPedidoExterno = 123L;
        PedidoDto pedidoDto = new PedidoDto();
        pedidoDto.setCdPedidoExterno(cdPedidoExterno);
        PedidoProduto pedidoProduto = new PedidoProduto();
        Produto produto = new Produto();
        produto.setCdProduto(1L);
        produto.setVlProduto(BigDecimal.TEN);
        pedidoProduto.setProduto(produto);
        pedidoProduto.setQuantidade(5L);
        pedidoDto.setItens(List.of(pedidoProduto));

        Pedido pedido = new Pedido();
        pedido.setItens(new ArrayList<>(pedidoDto.getItens()));
        pedido.getItens().forEach(item -> item.setPedido(pedido));
        pedido.setCdPedidoExterno(cdPedidoExterno);
        pedido.setVlTotalPedido(BigDecimal.valueOf(50));

        when(pedidoRepository.existsByCdPedidoExterno(cdPedidoExterno)).thenReturn(false);
        when(produtoRepository.findById(produto.getCdProduto())).thenReturn(Optional.of(new Produto()));
        when(pedidoMapper.toEntity(pedidoDto)).thenReturn(pedido);
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        PedidoDto resultado = pedidoService.criarPedido(pedidoDto);

        assertNotNull(resultado);
        assertEquals(cdPedidoExterno, resultado.getCdPedidoExterno());
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    void testFindPedidoByIdNonExistent() {
        Long cdPedidoNaoExistente = 999L;

        when(pedidoRepository.findById(cdPedidoNaoExistente)).thenReturn(Optional.empty());

        assertThrows(PedidoNotFoundException.class, () -> pedidoService.findPedidoById(cdPedidoNaoExistente));
        verify(pedidoRepository, times(1)).findById(cdPedidoNaoExistente);
    }

    @Test
    void testFindPedidoByIdExistent() {
        Long cdPedidoExistente = 123L;
        Pedido pedidoExistente = new Pedido();
        pedidoExistente.setCdPedido(cdPedidoExistente);
        PedidoDto pedidoDtoExistente = new PedidoDto();
        pedidoDtoExistente.setCdPedido(cdPedidoExistente);

        when(pedidoRepository.findById(cdPedidoExistente)).thenReturn(Optional.of(pedidoExistente));
        when(pedidoMapper.toDto(pedidoExistente)).thenReturn(pedidoDtoExistente);

        PedidoDto resultado = pedidoService.findPedidoById(cdPedidoExistente);

        assertNotNull(resultado);
        assertEquals(cdPedidoExistente, resultado.getCdPedido());
        verify(pedidoRepository, times(1)).findById(cdPedidoExistente);
        verify(pedidoMapper, times(1)).toDto(pedidoExistente);
    }

    @Test
    void testFindPedidosByFilterNoMatch() {
        PedidoFiltroDto pedidoFiltroDto = new PedidoFiltroDto();
        pedidoFiltroDto.setCdPedidoExterno(999L);

        when(pedidoRepository.findPedidosByFilter(pedidoFiltroDto)).thenReturn(new ArrayList<>());

        List<PedidoDto> resultado = pedidoService.findPedidosByFilter(pedidoFiltroDto);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(pedidoRepository, times(1)).findPedidosByFilter(pedidoFiltroDto);
    }

    @Test
    void testFindPedidosByFilterWithMatch() {
        Long cdPedidoExterno = 123L;
        PedidoFiltroDto pedidoFiltroDto = new PedidoFiltroDto();
        pedidoFiltroDto.setCdPedidoExterno(cdPedidoExterno);

        Pedido pedidoExistente = new Pedido();
        pedidoExistente.setCdPedidoExterno(cdPedidoExterno);
        PedidoDto pedidoDtoExistente = new PedidoDto();
        pedidoDtoExistente.setCdPedidoExterno(cdPedidoExterno);

        when(pedidoRepository.findPedidosByFilter(pedidoFiltroDto)).thenReturn(List.of(pedidoDtoExistente));
        when(pedidoMapper.toDto(pedidoExistente)).thenReturn(pedidoDtoExistente);

        List<PedidoDto> resultado = pedidoService.findPedidosByFilter(pedidoFiltroDto);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(cdPedidoExterno, resultado.get(0).getCdPedidoExterno());
        verify(pedidoRepository, times(1)).findPedidosByFilter(pedidoFiltroDto);
        verify(pedidoMapper, times(1)).toDto(pedidoExistente);
    }

    @Test
    void testCriarPedidoSemItensNaoDefineDtPedido() {
        Long cdPedidoExterno = 123L;
        PedidoDto pedidoDto = new PedidoDto();
        pedidoDto.setCdPedidoExterno(cdPedidoExterno);

        Pedido pedido = new Pedido();
        pedido.setCdPedidoExterno(cdPedidoExterno);

        when(pedidoRepository.existsByCdPedidoExterno(cdPedidoExterno)).thenReturn(false);
        when(pedidoMapper.toEntity(pedidoDto)).thenReturn(pedido);
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        PedidoDto resultado = pedidoService.criarPedido(pedidoDto);

        assertNotNull(resultado);
        assertEquals(cdPedidoExterno, resultado.getCdPedidoExterno());
        assertNull(resultado.getDtPedido());
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    void testCriarPedidoSemItensNaoCalculaValorTotal() {
        Long cdPedidoExterno = 123L;
        PedidoDto pedidoDto = new PedidoDto();
        pedidoDto.setCdPedidoExterno(cdPedidoExterno);

        Pedido pedido = new Pedido();
        pedido.setCdPedidoExterno(cdPedidoExterno);

        when(pedidoRepository.existsByCdPedidoExterno(cdPedidoExterno)).thenReturn(false);
        when(pedidoMapper.toEntity(pedidoDto)).thenReturn(pedido);
        when(pedidoRepository.save(pedido)).thenReturn(pedido);

        PedidoDto resultado = pedidoService.criarPedido(pedidoDto);

        assertNotNull(resultado);
        assertEquals(cdPedidoExterno, resultado.getCdPedidoExterno());
        assertNull(resultado.getVlTotalPedido());
        verify(pedidoRepository, times(1)).save(pedido);
    }
}
