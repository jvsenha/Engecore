package br.com.engecore.DTO;

import lombok.Data;

@Data
public class EstoqueMaterialResponse {
    private String estoque;
    private String material;
    private float quantidadeAtual;
    private float quantidadeMinima;
    private float quantidadeMaxima;
}
