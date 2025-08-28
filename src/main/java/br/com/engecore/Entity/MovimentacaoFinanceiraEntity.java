package br.com.engecore.Entity;

import br.com.engecore.Enum.TipoMov;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movimentacao_financeira")
public class MovimentacaoFinanceiraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idMov;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoMov tipo;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "obra_id")
    private ObrasEntity obraRelacionado;

    @ManyToOne
    @JoinColumn(name = "usuario_responsavel_id", nullable = false)
    private UserEntity usuarioResponsavel;
}
