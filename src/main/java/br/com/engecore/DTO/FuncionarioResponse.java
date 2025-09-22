package br.com.engecore.DTO;

import br.com.engecore.Enum.Role;
import br.com.engecore.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioResponse {
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String cargo;
    private BigDecimal salario;
    private LocalDate dataAdmissao;
    private Status status;
    private Role role;
}
