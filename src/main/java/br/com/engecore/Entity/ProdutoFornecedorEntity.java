package br.com.engecore.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private InsumoEntity insumo;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id", nullable = false)
    @JsonBackReference
    private FornecedorEntity fornecedor;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDate dataAtualizacao;

    @ManyToOne
    @JoinColumn(name = "marca_id", nullable = false)
    @JsonBackReference
    private MarcaEntity marca;

    @Column(name = "modelo", nullable = false)
    private String modelo;

    // CAMPOS ADICIONADOS PARA COMPLETAR O FLUXO DE COTAÇÃO

    @Column(length = 100)
    private String prazoEntrega; // Ex: "5 dias úteis"

    @Column(length = 100)
    private String condicaoPagamento; // Ex: "30 dias"

    @Column(columnDefinition = "TEXT")
    private String observacoes; // Ex: "Frete grátis acima de R$ 500"
}