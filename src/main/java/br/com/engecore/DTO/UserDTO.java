package br.com.engecore.DTO;

import br.com.engecore.Enum.Role;
import br.com.engecore.Enum.Status;
import br.com.engecore.Enum.TipoPessoa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long idUsuario;

    private String nome;
    private String email;
    private String senha;
    private String telefone;

    private Status status;
    private Role role;

    private TipoPessoa tipoPessoa; // FISICA ou JURIDICA

    // Campos PF
    private String cpf;
    private String rg;
    private LocalDate dataNascimento;

    // Campos PJ
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String inscricaoEstadual;
}
