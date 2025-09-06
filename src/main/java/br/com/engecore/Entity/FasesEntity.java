package br.com.engecore.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "fases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FasesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_termino")
    private LocalDate dataTermino;

    @Column(name = "tempo_esperado")
    private Integer tempoEsperado; // em dias, por exemplo

    @Column(name = "tempo_levado")
    private Integer tempoLevado;

    @Column(columnDefinition = "TEXT")
    private String descricao;
}
