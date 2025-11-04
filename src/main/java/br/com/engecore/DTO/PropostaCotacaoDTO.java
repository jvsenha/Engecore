package br.com.engecore.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropostaCotacaoDTO {

    // ID da PropostaCotacaoEntity (para o usuário poder selecioná-la)
    private Long id;

    // Nome Fantasia ou Razão Social do FornecedorEntity
    private String fornecedor;

    // CNPJ do FornecedorEntity
    private String cnpj;

    // Valor específico ofertado nesta proposta
    private BigDecimal valorUnitario;

    // Informação de texto vinda da PropostaCotacaoEntity
    private String prazoEntrega;

    // Informação de texto vinda da PropostaCotacaoEntity
    private String condicaoPagamento;

    // Observações específicas desta proposta
    private String observacoes;

    // Flag para o frontend destacar (ex: "fas fa-star")
    private boolean melhorPreco;
}