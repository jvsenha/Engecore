package br.com.engecore.DTO;

import br.com.engecore.Enum.TipoEstoque;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueResponse {
    private String nomeObra;
    private String nome;
    private TipoEstoque tipo;
    private List<String> estoqueMateriais;
}
