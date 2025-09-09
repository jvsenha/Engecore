package br.com.engecore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FasesDTO {
    private Long id;
    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataTermino;
    private Integer tempoEsperado;
    private Integer tempoLevado;
    private String descricao;
}
