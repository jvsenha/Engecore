package br.com.engecore.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "marca")
public class MarcaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    // Relacionamento com os produtos que esta marca possui em fornecedores
    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProdutoFornecedorEntity> produtosFornecidos;

    // Relacionamento com os itens de estoque desta marca
    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MaterialEstoque> materiaisEmEstoque;
}