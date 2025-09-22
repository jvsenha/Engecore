package br.com.engecore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FasesResponse {
    private Long id;
    private String nome;
    private Long idobra;
    private LocalDate dataInicio;
    private LocalDate dataTermino;
    private Integer tempoEsperado;
    private Integer tempoLevado;
    private String descricao;

}
