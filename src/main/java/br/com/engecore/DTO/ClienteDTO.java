package br.com.engecore.DTO;

import br.com.engecore.Enum.TipoPessoa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.engecore.Entity.EnderecoEmbeddable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO extends UserDTO {
    private String cpf;
    private EnderecoEmbeddable endereco;
    private List<Long> obrasIds;
    private TipoPessoa tipoPessoa;
    private String cpfCnpj;
}
