package br.com.engecore.DTO;

import br.com.engecore.Entity.EnderecoEmbeddable;
import br.com.engecore.Entity.FasesEntity;
import br.com.engecore.Enum.FaixaRenda;
import br.com.engecore.Enum.ProgramaSocial;
import br.com.engecore.Enum.StatusConst;
import br.com.engecore.Enum.TipoObra;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ObrasDTO {

    private Long idObra;

    // Informações básicas da obra
    private String nomeObra;
    private EnderecoEmbeddable endereco;
    private StatusConst status; // andamento, concluída, etc.
    private TipoObra tipo; // residencial, comercial, social, etc.

    // Informações sobre cliente e responsável
    private Long clienteId;
    private Long responsavelId;
    private String clienteNome;
    private String responsavelNome;

    // Dados de unidades (para obras sociais)
    private Integer totalUnidades;           // número total de unidades
    private Integer unidadesConcluidas;      // unidades já concluídas

    // Informações financeiras
    private Double valorTotal;               // valor total da obra
    private Double valorLiberado;            // valor já liberado pelo financiamento ou convênio
    private Double pagosFornecedores;        // valor pago a fornecedores
    private Double custoPorUnidade;          // custo médio por unidade

    // Programas sociais
    private FaixaRenda faixaRenda;           // faixa de renda se for obra social
    private Boolean documentacaoAprovada;    // validação de documentação/convênios
    private ProgramaSocial programaSocial;

    // Datas importantes
    private LocalDate dataInicio;
    private LocalDate dataPrevistaConclusao;
    private LocalDate dataConclusaoReal;

    // Blocos ou fases da obra
    private List<FasesEntity> fases;


}
