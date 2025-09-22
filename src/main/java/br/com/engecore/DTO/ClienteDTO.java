package br.com.engecore.DTO;

import br.com.engecore.Entity.EnderecoEmbeddable;
import br.com.engecore.Enum.Role;
import br.com.engecore.Enum.Status;
import br.com.engecore.Enum.TipoPessoa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private Status status;
    private Role role;
    private TipoPessoa tipoPessoa;
    private EnderecoEmbeddable endereco;

    private List<Long> obrasIds;

    private String cpf;
    private String rg;
    private LocalDate dataNascimento;

    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String inscricaoEstadual;
}
