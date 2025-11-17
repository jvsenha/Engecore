package br.com.engecore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO "leve" para a tela de listagem de cotações.
 * Retorna apenas as informações essenciais para a visualização em tabela.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotacaoListDTO {

    // Identificação da Cotação
    private Long id;
    private String status;

    // O que é?
    private String nomeInsumo;
    private BigDecimal quantidade;

    // Para onde?
    private String nomeObra;

    // Urgência?
    private String prioridade;
    private LocalDate dataNecessidade;

    // Qual a melhor oferta?
    private Integer totalPropostas;
    private BigDecimal melhorValorUnitario;
    private String nomeMelhorFornecedor;
}