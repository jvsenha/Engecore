package br.com.engecore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para o relatório de Curva ABC de Insumos.
 * Mostra o custo total por insumo e seu percentual acumulado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurvaAbcDTO {

    private Long insumoId;
    private String insumoNome;
    private BigDecimal custoTotal;
    private double percentual; // (custoTotal / grandTotal) * 100
    private double percentualAcumulado; // soma dos percentuais
}