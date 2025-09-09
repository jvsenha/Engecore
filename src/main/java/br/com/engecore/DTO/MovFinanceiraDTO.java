package br.com.engecore.DTO;

import br.com.engecore.Entity.FuncionarioEntity;
import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Entity.ObrasEntity;
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
    private ObrasEntity obra;
    private InsumoEntity insumo;
    private FuncionarioEntity funcionarioResponsavel;
    private LocalDate dataMovimento;
    private String descricao;
}
