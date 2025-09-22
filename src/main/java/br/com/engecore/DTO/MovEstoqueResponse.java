package br.com.engecore.DTO;

import br.com.engecore.Enum.TipoMov;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovEstoqueResponse {
    private Long id;
    private String material;
    private Long estoqueOrigem;
    private Long estoqueDestino;
    private BigDecimal quantidade;
    private TipoMov tipoMov;
    private LocalDate dataMovimentacao;
    private Long funcionarioResponsavel;
}
