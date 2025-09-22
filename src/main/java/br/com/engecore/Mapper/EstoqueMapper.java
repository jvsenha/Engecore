package br.com.engecore.Mapper;

import br.com.engecore.DTO.EstoqueDTO;
import br.com.engecore.DTO.EstoqueResponse;
import br.com.engecore.Entity.EstoqueEntity;
import org.springframework.stereotype.Component;

@Component
public class EstoqueMapper {

    // Entity → Response DTO
    public static EstoqueResponse toDTO(EstoqueEntity entity) {
        if (entity == null) return null;

        EstoqueResponse dto = new EstoqueResponse();
        dto.setNome(entity.getNome());
        dto.setTipo(entity.getTipo());

        if (entity.getObra() != null) {
            dto.setNomeObra(entity.getObra().getNomeObra());
        }

        if (entity.getEstoqueMateriais() != null) {
            dto.setEstoqueMateriais(
                    entity.getEstoqueMateriais().stream()
                            .map(mat -> mat.getMaterial().getNome()) // só pega o id do material no estoque
                            .toList()
            );
        }

        return dto;
    }

    // DTO → Entity (sem resolver ids aqui)
    public static EstoqueEntity toEntity(EstoqueDTO dto) {
        if (dto == null) return null;

        EstoqueEntity entity = new EstoqueEntity();
        entity.setNome(dto.getNome());
        entity.setTipo(dto.getTipo());
        // obra e materiais serão carregados no Service a partir dos IDs

        return entity;
    }
}
