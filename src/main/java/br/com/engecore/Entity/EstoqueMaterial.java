package br.com.engecore.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "estoque_material")
public class EstoqueMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "estoque_id", nullable = false)
    private EstoqueEntity estoque;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private MaterialEntity material;

    @Column(nullable = false)
    private float quantidadeAtual;

    @Column(nullable = false)
    private float quantidadeMinima;

    private Float quantidadeMaxima;

    // 🔹 Regra de negócio de estoque crítico
    public boolean isEstoqueCritico() {
        return quantidadeAtual <= quantidadeMinima;
    }
}
