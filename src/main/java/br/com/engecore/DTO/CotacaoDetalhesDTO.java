package br.com.engecore.DTO;

import br.com.engecore.Entity.InsumoEntity;
// Remova: import br.com.engecore.Entity.PropostaCotacaoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CotacaoDetalhesDTO {

    private Long id;
    private String nomeObra;

    private InsumoEntity insumo;

    private BigDecimal quantidade;
    private LocalDate dataNecessidade;
    private String prioridade;
    private String status;

    private String funcionarioSolicitante;

    // CORREÇÃO: Use o DTO aqui, não a Entity
    private List<PropostaCotacaoDTO> propostas;
}