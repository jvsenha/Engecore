package br.com.engecore.DTO;

import br.com.engecore.Enum.TipoPessoa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FornecedorDTO extends UserDTO {
    private String cpf;
    private String rg;
    private LocalDate dataNascimento;


    private String cnpj;
    private String razaoSocial;
    private String nomeFantasia;
    private String inscricaoEstadual;
}