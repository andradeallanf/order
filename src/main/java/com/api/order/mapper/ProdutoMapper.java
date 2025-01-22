package com.api.order.mapper;

import com.api.order.dto.ProdutoDto;
import com.api.order.model.Produto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    ProdutoDto toDto(Produto produto);

    Produto toEntity(ProdutoDto produtoDto);

    List<ProdutoDto> toDtoList(List<Produto> produtos);
}
