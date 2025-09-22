package br.com.engecore.Mapper;

import br.com.engecore.DTO.MovEstoqueDTO;
import br.com.engecore.DTO.MovEstoqueResponse;
import br.com.engecore.Entity.EstoqueEntity;
import br.com.engecore.Entity.FuncionarioEntity;
import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Entity.MovEstoqueEntity;
import org.springframework.stereotype.Component;

@Component
public class MovEstoqueMapper {

    // Entity → DTO
    public static MovEstoqueDTO toDTO(MovEstoqueEntity entity) {
        if (entity == null) return null;

        MovEstoqueDTO dto = new MovEstoqueDTO();
        dto.setId(entity.getId());
        dto.setTipoMov(entity.getTipoMov());
        dto.setInsumoId(entity.getInsumo().getId());
        dto.setEstoqueOrigemId(entity.getEstoqueOrigem() != null ? entity.getEstoqueOrigem().getId() : null);
        dto.setEstoqueDestinoId(entity.getEstoqueDestino() != null ? entity.getEstoqueDestino().getId() : null);
        dto.setQuantidade(entity.getQuantidade());
        dto.setDataMovimentacao(entity.getDataMovimentacao());
        dto.setFuncionarioId(entity.getFuncionarioResponsavel().getId());

        return dto;
    }

    public static MovEstoqueEntity toEntity(MovEstoqueDTO dto,
                                            InsumoEntity insumo,
                                            EstoqueEntity estoqueOrigem,
                                            EstoqueEntity estoqueDestino,
                                            FuncionarioEntity funcionario) {
        if (dto == null) return null;

        MovEstoqueEntity entity = new MovEstoqueEntity();
        entity.setId(dto.getId());
        entity.setTipoMov(dto.getTipoMov());
        entity.setInsumo(insumo);
        entity.setEstoqueOrigem(estoqueOrigem);
        entity.setEstoqueDestino(estoqueDestino);
        entity.setQuantidade(dto.getQuantidade());
        entity.setDataMovimentacao(dto.getDataMovimentacao());
        entity.setFuncionarioResponsavel(funcionario);

        return entity;
    }


    public static MovEstoqueResponse toResponse(MovEstoqueEntity entity) {
        MovEstoqueResponse response = new MovEstoqueResponse();
        response.setId(entity.getId());
        response.setMaterial(entity.getInsumo() != null ? entity.getInsumo().getNome() : null);
        response.setEstoqueOrigem(entity.getEstoqueOrigem() != null ? entity.getEstoqueOrigem().getId() : null);
        response.setEstoqueDestino(entity.getEstoqueDestino() != null ? entity.getEstoqueDestino().getId() : null);
        response.setQuantidade(entity.getQuantidade());
        response.setTipoMov(entity.getTipoMov());
        response.setDataMovimentacao(entity.getDataMovimentacao());
        response.setFuncionarioResponsavel(entity.getFuncionarioResponsavel() != null ? entity.getFuncionarioResponsavel().getId() : null);
        return response;
    }
}
