package br.com.engecore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO auxiliar para a query de custo de insumo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustoInsumoDTO {
    private Long insumoId;
    private String insumoNome;
    private BigDecimal custoTotal;
}