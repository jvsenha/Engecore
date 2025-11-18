package br.com.engecore.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO utilizado para receber atualizações de dados de um MaterialEstoque existente.
 * Não altera a quantidade, apenas os dados cadastrais/financeiros.
 */
@Data
@NoArgsConstructor
public class MaterialEstoqueUpdateDTO {

    @NotNull(message = "O valor não pode ser nulo.")
    @PositiveOrZero(message = "O valor deve ser zero ou positivo.")
    private BigDecimal valor;

    @PositiveOrZero(message = "A quantidade mínima deve ser zero ou positiva.")
    private BigDecimal quantidadeMinima; // Permite nulo se não for obrigatório

    @PositiveOrZero(message = "A quantidade máxima deve ser zero ou positiva.")
    private BigDecimal quantidadeMaxima; // Permite nulo se não for obrigatório

    @Size(max = 255, message = "O modelo não pode exceder 255 caracteres.")
    private String modelo;
}