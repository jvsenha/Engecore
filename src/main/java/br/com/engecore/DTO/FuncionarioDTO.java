package br.com.engecore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuncionarioDTO extends UserDTO {
    private String cargo;
    private Float salario;
    private LocalDate dataAdmissao;
}