package br.com.engecore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotacaoRequestDTO {

    // ID da ObrasEntity
    private Long obraId;

    // ID do InsumoEntity genérico
    private Long insumoId;

    // Quantidade necessária
    private BigDecimal quantidade;

    // Data que o material é necessário na obra
    private LocalDate dataNecessidade;

    // "Normal", "Alta", "Urgente"
    private String prioridade;

    // ID do FuncionarioEntity que está solicitando
    private Long funcionarioId;
}