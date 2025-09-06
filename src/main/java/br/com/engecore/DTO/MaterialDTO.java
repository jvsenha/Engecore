package br.com.engecore.DTO;

import br.com.engecore.Enum.Unidade;
import lombok.Data;

@Data
public class MaterialDTO {
    private String nome;
    private Unidade unidade;
}
