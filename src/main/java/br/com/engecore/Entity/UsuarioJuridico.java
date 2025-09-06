package br.com.engecore.Entity;

import br.com.engecore.Validation.CpfCnpj;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario_juridico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioJuridico {
    @Id
    private Long idUsuario;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idUsuario")
    @JsonBackReference
    private UserEntity usuario;


    @Column(nullable = false, unique = true, length = 20)
    @CpfCnpj
    private String cnpj;

    @Column(nullable = false)
    private String razaoSocial;

    private String nomeFantasia;

    private String inscricaoEstadual;
}
