package com.api.order.controller;

import com.api.order.dto.PedidoDto;
import com.api.order.dto.ProdutoDto;
import com.api.order.service.ProdutoService;
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
@RequestMapping("/api/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Operation(
            summary = "Cria um novo produto",
            description = "Este endpoint permite criar um novo produto com informações como nome, valor e data de cadastro."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoDto.class))),
            @ApiResponse(responseCode = "400", description = "Erro na validação do produto", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PostMapping("/criar-produto/")
    public ResponseEntity<ProdutoDto> criarProduto(@RequestBody ProdutoDto produtoDto) {
        ProdutoDto produtoCriado = produtoService.criarProduto(produtoDto);
        return ResponseEntity.ok(produtoCriado);
    }

    @Operation(
            summary = "Atualiza um produto existente",
            description = "Este endpoint permite atualizar as informações de um produto já existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoDto.class))),
            @ApiResponse(responseCode = "400", description = "Erro na validação do produto", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @PostMapping("/atualizar-produto/")
    public ResponseEntity<ProdutoDto> atualizarProduto(@RequestBody ProdutoDto produtoDto) {
        ProdutoDto produtoAtualizado = produtoService.atualizarProduto(produtoDto);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @Operation(
            summary = "Deleta um produto pelo ID",
            description = "Este endpoint permite excluir um produto com base no ID fornecido."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @DeleteMapping("/{id}/")
    public ResponseEntity<Void> deleteById(@PathVariable Long cdProduto) {
        produtoService.deleteProduto(cdProduto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Busca um produto pelo ID",
            description = "Este endpoint retorna os detalhes de um produto específico com base no ID informado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoDto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<ProdutoDto> getProdutoById(Long cdProduto) {
        ProdutoDto produto = produtoService.findProdutoById(cdProduto);
        return ResponseEntity.ok(produto);
    }

    @Operation(
            summary = "Busca todos os produtos",
            description = "Este endpoint retorna uma lista de todos os produtos cadastrados no sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produtos encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProdutoDto.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    @GetMapping("/all/")
    public ResponseEntity<List<ProdutoDto>> findAll() {
        List<ProdutoDto> produto = produtoService.findAll();
        return ResponseEntity.ok(produto);
    }
}
