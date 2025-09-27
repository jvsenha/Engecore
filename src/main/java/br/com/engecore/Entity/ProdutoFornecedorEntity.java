package br.com.engecore.Entity;

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
@Table(name = "produto_fornecedor")
public class ProdutoFornecedorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "insumo_id", nullable = false)
    private InsumoEntity insumo;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private FornecedorEntity fornecedor;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDate dataAtualizacao;

}