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
@Table(name = "funcionario")
public class FuncionarioEntity extends UserEntity {

    @Column(name = "cargo", nullable = false)
    private String cargo;

    @Column(name = "salario", nullable = false)
    private BigDecimal salario;

    @Column(name = "dataAdmissao", nullable = false)
    private LocalDate dataAdmissao;
}
