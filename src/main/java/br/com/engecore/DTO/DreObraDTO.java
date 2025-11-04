package br.com.engecore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object para o relatório DRE (Demonstrativo de Resultado) da Obra.
 * Compara o valor contratado com os custos já realizados.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DreObraDTO {

    private Long obraId;
    private String nomeObra;
    private BigDecimal valorContratado; // Valor total orçado/contratado
    private BigDecimal custoTotalRealizado; // Soma de todas as despesas
    private BigDecimal saldo; // (valorContratado - custoTotalRealizado)
    private String statusObra; // Status atual da obra (ex: EM_ANDAMENTO)
}