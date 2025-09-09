package br.com.engecore.DTO;

import br.com.engecore.Entity.EstoqueEntity;
import br.com.engecore.Entity.FuncionarioEntity;
import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Enum.TipoMov;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovEstoqueDTO {
    private InsumoEntity insumo;
    private EstoqueEntity estoqueOrigem;
    private EstoqueEntity estoqueDestino;
    private float quantidade;
    private TipoMov tipoMov;
    private LocalDate dataMovimentacao;
    private FuncionarioEntity funcionarioResponsavel;
}
