package br.com.engecore.Mapper;

import br.com.engecore.DTO.MaterialEstoqueRequest;
import br.com.engecore.DTO.MaterialEstoqueResponse;
import br.com.engecore.Entity.EstoqueEntity;
import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Entity.MaterialEstoque;
import br.com.engecore.Repository.EstoqueRepository;
import br.com.engecore.Repository.InsumoRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MaterialEstoqueMapper {

    private final InsumoRepository insumoRepository;
    private final EstoqueRepository estoqueRepository;


    public MaterialEstoqueMapper(InsumoRepository insumoRepository, EstoqueRepository estoqueRepository) {
        this.insumoRepository = insumoRepository;
        this.estoqueRepository = estoqueRepository;
    }

    // DTO -> Entity
    public MaterialEstoque toEntity(MaterialEstoqueRequest request) {
        if (request == null) {
            return null;
        }

        MaterialEstoque entity = new MaterialEstoque();

        InsumoEntity insumo = insumoRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new RuntimeException("Insumo não encontrado!"));

        EstoqueEntity estoque = estoqueRepository.findById(request.getEstoqueId())
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado!"));

        entity.setMaterial(insumo);
        entity.setEstoque(estoque);
        entity.setQuantidadeAtual(request.getQuantidadeAtual());
        entity.setQuantidadeMinima(request.getQuantidadeMinima());
        entity.setQuantidadeMaxima(request.getQuantidadeMaxima());

        return entity;
    }

    // Entity -> DTO Response
    public static MaterialEstoqueResponse toResponse(MaterialEstoque entity) {
        if (entity == null) {
            return null;
        }

        MaterialEstoqueResponse response = new MaterialEstoqueResponse();
        response.setEstoque(entity.getEstoque().getNome());
        response.setMaterial(entity.getMaterial().getNome());
        response.setQuantidadeAtual(entity.getQuantidadeAtual());
        response.setQuantidadeMinima(entity.getQuantidadeMinima());
        response.setQuantidadeMaxima(entity.getQuantidadeMaxima() != null ? entity.getQuantidadeMaxima() : BigDecimal.ZERO);

        return response;
    }
}