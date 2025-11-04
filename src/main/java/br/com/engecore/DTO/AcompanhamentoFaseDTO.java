package br.com.engecore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para o relatório de Acompanhamento Físico das Fases da Obra.
 * Mostra o progresso e o status de prazo de cada fase.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcompanhamentoFaseDTO {

    private Long id;
    private String nomeFase;
    private String statusPrazo; // Ex: "Concluída", "Em Dia", "Atrasada", "Concluída com Atraso"
    private LocalDate dataInicio;
    private LocalDate dataPrevistaTermino; // Calculada (dataInicio + tempoEsperado)
    private LocalDate dataTerminoReal;     // A data que realmente terminou
    private Integer tempoEsperadoDias;
    private Integer tempoLevadoDias;
    private String descricao;
}