package br.com.engecore.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoFornecedorDTO {
    // ID do Insumo genérico (ex: "Cimento CPII")
    private Long insumoId;

    // ID do Fornecedor que está vendendo
    private Long fornecedorId;

    // ID da Marca (ex: "Votorantim")
    private Long marcaId;

    // Campos específicos do produto
    private String modelo;
    private BigDecimal valor;
    private String prazoEntrega;
    private String condicaoPagamento;
    private String observacoes;
}