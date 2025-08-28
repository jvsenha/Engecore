package br.com.engecore.Entity;

import br.com.engecore.Enum.StatusConst;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "obras")
public class ObrasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idObra;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "endereco", nullable = false)
    private EnderecoEmbeddable endereco;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_const", nullable = false)
    private StatusConst statusConst;

    @Column(name = "dataInicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "dataFim", nullable = false)
    private LocalDate dataFim;

    @ManyToOne
    @JoinColumn(name = "responsavel_id", nullable = false)
    private FuncionarioEntity responsavel;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private ClienteEntity cliente;
}
