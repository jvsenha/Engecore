package br.com.engecore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotacaoRequestDTO {

    private Long obraId;

    private List<ItemCotacaoDTO> itens;

    private LocalDate dataNecessidade;

    private String prioridade;

    private Long funcionarioId;
}