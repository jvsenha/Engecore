package br.com.engecore.Entity;

import br.com.engecore.Enum.FaixaRenda;
import br.com.engecore.Enum.ProgramaSocial;
import br.com.engecore.Enum.StatusConst;
import br.com.engecore.Enum.TipoObra;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "obras")
public class ObrasEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idObra;

    // Informações básicas da obra
    private String nomeObra;

    @Embedded
    private EnderecoEmbeddable endereco;

    @Enumerated(EnumType.STRING)
    private StatusConst statusConst; // andamento, concluída, etc.

    @Enumerated(EnumType.STRING)
    private TipoObra tipo; // residencial, comercial, social, etc.

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonBackReference
    private ClienteEntity cliente;

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private FuncionarioEntity responsavel;

    // Dados de unidades
    private Integer totalUnidades;
    private Integer unidadesConcluidas;

    // Informações financeiras
    private BigDecimal valorTotal;
    private BigDecimal valorLiberado;
    private BigDecimal pagosFornecedores;


    private BigDecimal custoPorUnidade;

    // Programas sociais
    @Enumerated(EnumType.STRING)
    private FaixaRenda faixaRenda;
    private Boolean documentacaoAprovada;
    private ProgramaSocial programaSocial;

    // Datas importantes
    private LocalDate dataInicio;
    private LocalDate dataPrevistaConclusao;
    private LocalDate dataConclusaoReal;

    // Blocos ou fases da obra
    @ElementCollection
    @CollectionTable(name = "obras_fases", joinColumns = @JoinColumn(name = "obra_id"))
    @Column(name = "fase")
    private List<FasesEntity> fases;


    @OneToOne
    @JoinColumn(name = "estoque_id")
    private EstoqueEntity estoque; // Estoque da obra
}
