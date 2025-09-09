package br.com.engecore.DTO;

import br.com.engecore.Entity.EstoqueMaterial;
import br.com.engecore.Enum.TipoEstoque;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueDTO {
    private String nome;
    private TipoEstoque tipo;
    private List<EstoqueMaterial> estoqueMateriais;
}
