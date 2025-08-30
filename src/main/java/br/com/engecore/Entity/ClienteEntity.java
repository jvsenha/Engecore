package br.com.engecore.Entity;

import br.com.engecore.Enum.TipoPessoa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cliente")
public class ClienteEntity extends UserEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pessoa", nullable = false)
    private TipoPessoa tipoPessoa;

    @NotBlank(message = "CPF ou CNPJ é obrigatório")
    @Column(name = "cpf_cnpj", nullable = false, unique = true)
    private String cpfCnpj;

    @Column(name = "endereco", nullable = false)
    private EnderecoEmbeddable endereco;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ObrasEntity> obras;

    // Método auxiliar para validar o CPF ou CNPJ
    public boolean isCpfCnpjValido() {
        if (tipoPessoa == TipoPessoa.FISICA) {
            return validaCPF(cpfCnpj);
        } else if (tipoPessoa == TipoPessoa.JURIDICA) {
            return validaCNPJ(cpfCnpj);
        }
        return false;
    }

    private boolean validaCPF(String cpf) {
        // lógica de validação de CPF
        return cpf != null && cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }

    private boolean validaCNPJ(String cnpj) {
        // lógica de validação de CNPJ
        return cnpj != null && cnpj.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
    }
}
