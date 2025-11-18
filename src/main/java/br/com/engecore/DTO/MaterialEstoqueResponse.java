package br.com.engecore.DTO;

import br.com.engecore.Enum.Unidade;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MaterialEstoqueResponse {
    private Long materialEstoqueId; // ID do registro específico no estoque

    private Long insumoId;          // ID do Insumo genérico
    private String material;        // Nome do Insumo
    private Unidade unidade;        // Unidade (kg, un, m)

    private Long marcaId;           // NECESSÁRIO PARA TRANSFERÊNCIA
    private String marcaNome;
    private String modelo;          // NECESSÁRIO PARA TRANSFERÊNCIA
    private BigDecimal valor;       // NECESSÁRIO PARA TRANSFERÊNCIA (Custo)

    private String estoque;         // Nome do estoque onde está

    private BigDecimal quantidadeAtual;
    private BigDecimal quantidadeMinima;
    private BigDecimal quantidadeMaxima;

    private boolean estoqueCritico;
}
