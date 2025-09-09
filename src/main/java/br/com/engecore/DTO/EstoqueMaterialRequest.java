package br.com.engecore.DTO;

import br.com.engecore.Entity.EstoqueEntity;
import br.com.engecore.Entity.InsumoEntity;
import lombok.Data;

@Data
public class EstoqueMaterialRequest {
    private EstoqueEntity estoque;
    private InsumoEntity material;
    private float quantidadeAtual;
    private float quantidadeMinima;
    private Float quantidadeMaxima;
}
