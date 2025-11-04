package br.com.engecore.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "material_estoque")
public class MaterialEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "estoque_id", nullable = false)
    @JsonManagedReference
    private EstoqueEntity estoque;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    @JsonManagedReference
    private InsumoEntity material;

    @Column(nullable = false)
    private BigDecimal quantidadeAtual;

    @Column(nullable = false)
    private BigDecimal quantidadeMinima;

    @Column(nullable = false)
    private BigDecimal valor;

    private BigDecimal quantidadeMaxima;

    @ManyToOne
    @JoinColumn(name = "marca_id", nullable = false)
    private MarcaEntity marca;

    @Column(name = "modelo", nullable = false)
    private String modelo;

    public boolean isEstoqueCritico() {
        return quantidadeAtual.compareTo(quantidadeMinima) <= 0;
    }
}
