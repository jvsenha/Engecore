package br.com.engecore.DTO;

import br.com.engecore.Enum.CategoriaFinanceira;
import br.com.engecore.Enum.TipoMovFinanceiro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovFinanceiraDTO {
    private BigDecimal valor;
    private TipoMovFinanceiro tipo;
    private CategoriaFinanceira categoriaFinanceira;
    private Long obraId; // Alterado de ObrasEntity para Long
    private Long insumoId; // Alterado de InsumoEntity para Long
    private Long funcionarioResponsavelId; // Alterado de FuncionarioEntity para Long
    private Long clienteId; // Adicionado para movimentações diretas do cliente
    private LocalDate dataMovimento;
    private String descricao;
}
