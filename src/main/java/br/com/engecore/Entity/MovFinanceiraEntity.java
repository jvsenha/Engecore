package br.com.engecore.Entity;

import br.com.engecore.Enum.CategoriaFinanceira;
import br.com.engecore.Enum.TipoMovFinanceiro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movimentacao_financeira")
public class MovFinanceiraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimento;

    @Column(nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovFinanceiro tipo; // RECEITA ou DESPESA

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaFinanceira categoriaFinanceira; // Nova categoria adicionada

    @ManyToOne
    @JoinColumn(name = "obra_id")
    private ObrasEntity obra; // opcional, se a despesa ou receita for de uma obra específica

    @ManyToOne
    @JoinColumn(name = "insumo_id")
    private InsumoEntity insumo; // opcional, se a despesa for referente a um insumo

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private FuncionarioEntity funcionarioResponsavel; // quem registrou a movimentação

    @Column(nullable = false)
    private LocalDate dataMovimento;

    @Column(length = 255)
    private String descricao; // descrição da movimentação
}
