package br.com.engecore.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialEstoqueResponse {
    private String estoque;
    private String material;
    private BigDecimal quantidadeAtual;
    private BigDecimal quantidadeMinima;
    private BigDecimal quantidadeMaxima;
}
