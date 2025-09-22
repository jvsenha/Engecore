package br.com.engecore.Entity;

import br.com.engecore.Enum.TipoMov;
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
@Table(name = "movimentacao_estoque")
public class MovEstoqueEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // Material movimentado
        @ManyToOne
        @JoinColumn(name = "material_id", nullable = false)
        private InsumoEntity insumo;

        // Estoque de origem (pode ser null se for ENTRADA)
        @ManyToOne
        @JoinColumn(name = "estoque_origem_id")
        private EstoqueEntity estoqueOrigem;

        // Estoque de destino (pode ser null se for SAÍDA)
        @ManyToOne
        @JoinColumn(name = "estoque_destino_id")
        private EstoqueEntity estoqueDestino;

        // Quantidade movimentada
        @Column(nullable = false)
        private BigDecimal quantidade;

        // Tipo da movimentação: ENTRADA, SAIDA ou TRANSFERENCIA
        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private TipoMov tipoMov;

        // Data da movimentação
        @Column(nullable = false)
        private LocalDate dataMovimentacao;

        // Funcionário responsável pela movimentação
        @ManyToOne
        @JoinColumn(name = "funcionario_id", nullable = false)
        private FuncionarioEntity funcionarioResponsavel;
}
