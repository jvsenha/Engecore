package br.com.engecore.Mapper;

import br.com.engecore.DTO.EstoqueMaterialRequest;
import br.com.engecore.DTO.EstoqueMaterialResponse;
import br.com.engecore.Entity.EstoqueMaterial;

public class EstoqueMaterialMapper {

    // Converte DTO Request -> Entity
    public static EstoqueMaterial toEntity(EstoqueMaterialRequest request) {
        if (request == null) {
            return null;
        }

        EstoqueMaterial entity = new EstoqueMaterial();
        entity.setEstoque(request.getEstoque());
        entity.setMaterial(request.getMaterial());
        entity.setQuantidadeAtual(request.getQuantidadeAtual());
        entity.setQuantidadeMinima(request.getQuantidadeMinima());
        entity.setQuantidadeMaxima(request.getQuantidadeMaxima());

        return entity;
    }

    // Converte Entity -> DTO Response
    public static EstoqueMaterialResponse toResponse(EstoqueMaterial entity) {
        if (entity == null) {
            return null;
        }

        EstoqueMaterialResponse response = new EstoqueMaterialResponse();
        response.setEstoque(entity.getEstoque().getNome());   // supondo que EstoqueEntity tem um atributo nome
        response.setMaterial(entity.getMaterial().getNome()); // supondo que MaterialEntity tem um atributo nome
        response.setQuantidadeAtual(entity.getQuantidadeAtual());
        response.setQuantidadeMinima(entity.getQuantidadeMinima());
        response.setQuantidadeMaxima(
                entity.getQuantidadeMaxima() != null ? entity.getQuantidadeMaxima() : 0
        );

        return response;
    }
}
