package br.com.engecore.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "proposta_cotacao")
public class PropostaCotacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cotacao_id")
    @JsonBackReference
    private CotacaoEntity cotacao;

    // Liga a proposta ao produto específico (que tem fornecedor, marca e preço)
    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_fornecedor_id")
    @JsonBackReference
    private ProdutoFornecedorEntity produtoFornecedor;

    // Você pode copiar o valor de ProdutoFornecedor ou permitir que seja
    // sobrescrito aqui, caso o fornecedor dê um desconto para esta cotação específica.
    @Column(nullable = false)
    private BigDecimal valorUnitario;

    @Column(length = 100)
    private String prazoEntrega; // Ex: "5 dias úteis"

    @Column(length = 100)
    private String condicaoPagamento; // Ex: "30 dias"

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    private boolean melhorPreco; // Para destacar no frontend
}