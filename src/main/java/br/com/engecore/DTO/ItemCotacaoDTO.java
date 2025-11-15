package br.com.engecore.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemCotacaoDTO {
    private Long insumoId;
    private BigDecimal quantidade;
}
