package br.com.engecore.DTO;

import br.com.engecore.Enum.Role;
import br.com.engecore.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long idUsuario;
    private String nome;
    private String senha;
    private String email;
    private String telefone;
    private Status status;
    private Role role;
}
