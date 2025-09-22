package br.com.engecore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuncionarioDTO extends UserDTO {
    private String cargo;
    private BigDecimal salario;
    private LocalDate dataAdmissao;
    private String cpf;
    private String rg;
    private LocalDate dataNascimento;
    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String inscricaoEstadual;
}