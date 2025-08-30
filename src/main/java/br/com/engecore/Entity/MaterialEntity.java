package br.com.engecore.Entity;

import br.com.engecore.Enum.Unidade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "material")
public class MaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idMaterial;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidade", nullable = false)
    private Unidade unidade;

    @Column(name = "quantidadeAtual", nullable = false)
    private float quantidadeAtual;

    @Column(name = "quantidadeMinima", nullable = false)
    private float quantidadeMinima;

    // Estoque ao qual o material pertence
    @ManyToOne
    @JoinColumn(name = "estoque_id", nullable = false)
    private EstoqueEntity estoque;
}
