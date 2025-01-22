package com.api.order.controller;

import com.api.order.dto.PedidoDto;
import com.api.order.dto.PedidoFiltroDto;
import com.api.order.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(
            summary = "Cria um novo pedido",
            description = "Este endpoint permite criar um novo pedido com produtos associados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDto.class))),
            @ApiResponse(responseCode = "400", description = "Erro na validação do pedido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PostMapping("/criar-pedido/")
    public ResponseEntity<PedidoDto> criarPedido(@RequestBody PedidoDto pedidoDto) {
        PedidoDto pedidoCriado = pedidoService.criarPedido(pedidoDto);
        return ResponseEntity.ok(pedidoCriado);
    }

    @Operation(
            summary = "Busca um pedido por ID",
            description = "Este endpoint retorna os detalhes de um pedido específico com base no ID informado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDto.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<PedidoDto> getPedidoById(Long cdPedido) {
        PedidoDto pedido = pedidoService.findPedidoById(cdPedido);
        return ResponseEntity.ok(pedido);
    }

    @Operation(
            summary = "Filtra pedidos com base em critérios",
            description = "Retorna uma lista de pedidos que atendem aos critérios informados no filtro."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDto.class))),
            @ApiResponse(responseCode = "400", description = "Erro na validação do filtro", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/filter/")
    public ResponseEntity<List<PedidoDto>> getPedidoByFilter(@RequestBody PedidoFiltroDto pedidoFiltroDto) {
        List<PedidoDto> listPedidos = pedidoService.findPedidosByFilter(pedidoFiltroDto);
        return ResponseEntity.ok(listPedidos);
    }
}
