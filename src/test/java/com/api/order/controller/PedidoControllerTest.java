package com.api.order.controller;

import com.api.order.dto.PedidoDto;
import com.api.order.dto.PedidoFiltroDto;
import com.api.order.dto.PedidoProdutoDto;
import com.api.order.dto.ProdutoDto;
import com.api.order.exceptions.PedidoValidationException;
import com.api.order.message.producer.KafkaProducerMessage;
import com.api.order.model.Pedido;
import com.api.order.model.PedidoProduto;
import com.api.order.model.Produto;
import com.api.order.service.PedidoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoControllerTest {

    @Mock
    private PedidoService pedidoService;

    @Mock
    private KafkaProducerMessage kafkaProducerMessage;

    @InjectMocks
    private PedidoController pedidoController;

    @Test
    void criarPedido_InvalidPedidoDto_ThrowsPedidoValidationException() {
        PedidoDto invalidPedidoDto = new PedidoDto();

        PedidoValidationException exception = assertThrows(PedidoValidationException.class, () -> {
            pedidoController.criarPedido(invalidPedidoDto);
        });

        assertEquals("Pedido externo não informado", exception.getMessage());

        verify(kafkaProducerMessage, never()).sendMessage(any(PedidoDto.class), anyString());
    }

    @Test
    void getPedidoById_ThrowsException_ReturnsInternalServerError() {
        Long id = 1L;
        when(pedidoService.findPedidoById(id)).thenThrow(new RuntimeException("Some error message"));

        ResponseEntity<PedidoDto> response = pedidoController.getPedidoById(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(kafkaProducerMessage, never()).sendMessage(any(PedidoDto.class), anyString());
    }


    @Test
    void getPedidoByFilter_NoPedidosFound_ReturnsEmptyList() {
        PedidoFiltroDto pedidoFiltroDto = new PedidoFiltroDto();
        when(pedidoService.findPedidosByFilter(pedidoFiltroDto)).thenReturn(Collections.emptyList());

        ResponseEntity<List<PedidoDto>> response = pedidoController.getPedidoByFilter(pedidoFiltroDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(kafkaProducerMessage, never()).sendMessage(any(PedidoDto.class), anyString());
    }


    @Test
    void criarPedido_ValidPedidoDto_ReturnsCreated() {

        Pedido pedido = new Pedido();
        pedido.setCdPedidoExterno(12345L);
        pedido.setNmComprador("John Doe");
        pedido.setDtPedido(LocalDateTime.now());
        pedido.setStatusPedido("PENDENTE");
        Produto produto1 = new Produto(1L, "produto 1", BigDecimal.valueOf(100.0), LocalDateTime.now());
        Produto produto2 = new Produto(2L, "produto 2", BigDecimal.valueOf(100.0), LocalDateTime.now());
        PedidoProduto pedidoProduto1 = new PedidoProduto(null, pedido, produto1, 2L);
        PedidoProduto pedidoProduto2 = new PedidoProduto(null, pedido, produto2, 3L);
        List<PedidoProduto> pedidoProdutoList = new ArrayList<PedidoProduto>();
        pedidoProdutoList.add(pedidoProduto1);
        pedidoProdutoList.add(pedidoProduto2);
        pedido.setItens(pedidoProdutoList);
        pedido.setVlTotalPedido(BigDecimal.valueOf(250.0));

        ResponseEntity<PedidoDto> response = pedidoController.criarPedido(pedido.toDto());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(kafkaProducerMessage, times(1)).sendMessage(pedido.toDto(), "pedido");
    }
}