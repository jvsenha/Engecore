package br.com.engecore.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialEstoqueRequest {
    private Long estoqueId;
    private Long materialId;
    private BigDecimal quantidadeAtual;
    private BigDecimal quantidadeMinima;
    private BigDecimal quantidadeMaxima;
    private BigDecimal valor;
}
