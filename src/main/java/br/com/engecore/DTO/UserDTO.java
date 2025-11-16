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

    private Long id;

    private String nome;
    private String email;
    private String senha;
    private String telefone;

    private Status status;
    private Role role;

    private TipoPessoa tipoPessoa;

    private String cpf;
    private String rg;
    private LocalDate dataNascimento;
}
