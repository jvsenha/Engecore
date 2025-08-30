package br.com.engecore.Entity;

import br.com.engecore.Enum.TipoEstoque;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "estoque")
public class EstoqueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstoque;

    private String nome; // Ex: "Estoque Obra 01", "Estoque Empresa"

    @Enumerated(EnumType.STRING)
    private TipoEstoque tipo; // OBRA ou EMPRESA

    @OneToMany(mappedBy = "estoque")
    private List<MaterialEntity> materiais;

}
