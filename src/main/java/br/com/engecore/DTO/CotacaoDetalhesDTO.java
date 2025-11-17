package br.com.engecore.DTO;

import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Entity.PropostaCotacaoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para a tela de "Detalhes da Cotação" (GET /cotacoes/{id}).
 * Retorna os campos exatos solicitados pelo usuário.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotacaoDetalhesDTO {

    private Long id;
    private String nomeObra;

    // Retorna o objeto Insumo completo
    private InsumoEntity insumo;

    private BigDecimal quantidade;
    private LocalDate dataNecessidade;
    private String prioridade;
    private String status;

    // Retorna apenas o nome do funcionário
    private String funcionarioSolicitante;

    // Retorna a lista de Propostas completa
    private List<PropostaCotacaoEntity> propostas;
}