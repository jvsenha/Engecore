package br.com.engecore.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoFornecedorDTO {

    // ID da própria entidade ProdutoFornecedor (útil para edição/deleção)
    private Long id;

    // ID do Insumo genérico (ex: "Cimento CPII")
    private Long insumoId;
    // NOME do Insumo (para exibição)
    private String insumoNome;

    // ID do Fornecedor que está vendendo
    private Long fornecedorId;

    // ID da Marca (ex: "Votorantim")
    private Long marcaId;
    // NOME da Marca (para exibição)
    private String marcaNome;

    // Campos específicos do produto
    private String modelo;
    private BigDecimal valor;
    private String prazoEntrega;
    private String condicaoPagamento;
    private String observacoes;
}