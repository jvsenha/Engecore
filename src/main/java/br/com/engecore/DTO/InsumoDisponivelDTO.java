package br.com.engecore.DTO;

import br.com.engecore.Enum.Unidade;

import java.math.BigDecimal;

public record InsumoDisponivelDTO(
        Long materialEstoqueId, // ID da entidade MaterialEstoque
        Long insumoId,          // ID do Insumo (mestre)
        String insumoNome,
        Unidade unidade,
        BigDecimal quantidadeAtual, // A informação mais importante
        BigDecimal quantidadeMinima,
        BigDecimal quantidadeMaxima,
        BigDecimal valorUni,
        Long marcaId,       // Informação extra útil
        String marcaNome,       // Informação extra útil
        String modelo,
        boolean estoqueCritico) {
}
