package br.com.engecore.DTO;

import br.com.engecore.Enum.Unidade;
import lombok.Data;

@Data
public class InsumoDTO {
    private String nome;
    private Unidade unidade;
    private String marca;
    private String modelo;
}
