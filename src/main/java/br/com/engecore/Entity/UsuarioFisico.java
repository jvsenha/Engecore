package br.com.engecore.Entity;

import br.com.engecore.Validation.CpfCnpj;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "usuario_fisico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioFisico {

    @Id
    private Long idUsuario; // mesmo id do usuário base

    @OneToOne
    @MapsId
    @JoinColumn(name = "idUsuario")
    @JsonBackReference
    private UserEntity usuario;

    @Column(nullable = false, unique = true, length = 11)
    @CpfCnpj
    private String cpf;

    private String rg;

    private LocalDate dataNascimento;
}
