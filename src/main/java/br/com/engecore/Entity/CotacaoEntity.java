package br.com.engecore.Entity;

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
@Table(name = "cotacao")
public class CotacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "obra_id")
    private ObrasEntity obra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "insumo_id")
    private InsumoEntity insumo; // O insumo genérico que está sendo cotado

    @Column(nullable = false)
    private BigDecimal quantidade;

    @Column(nullable = false)
    private LocalDate dataNecessidade;

    @Column(length = 50)
    private String prioridade; // "Normal", "Alta", "Urgente"

    @Column(length = 50)
    private String status; // Ex: "ABERTA", "CONCLUIDA", "CANCELADA"

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private FuncionarioEntity funcionarioSolicitante;

    // A mágica acontece aqui: uma cotação tem VÁRIAS propostas
    @OneToMany(mappedBy = "cotacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropostaCotacaoEntity> propostas;
}