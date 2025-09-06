package br.com.engecore.DTO;

import br.com.engecore.Entity.EstoqueEntity;
import br.com.engecore.Entity.MaterialEntity;
import lombok.Data;

@Data
public class EstoqueMaterialRequest {
    private EstoqueEntity estoque;
    private MaterialEntity material;
    private float quantidadeAtual;
    private float quantidadeMinima;
    private Float quantidadeMaxima;
}
