package br.com.engecore.Mapper;

import br.com.engecore.DTO.MovEstoqueDTO;
import br.com.engecore.Entity.MovEstoqueEntity;

public class MovEstoqueMapper {

    // Entity → DTO
    public static MovEstoqueDTO toDTO(MovEstoqueEntity entity) {
        if (entity == null) return null;

        MovEstoqueDTO dto = new MovEstoqueDTO();
        dto.setInsumo(entity.getInsumo());
        dto.setEstoqueOrigem(entity.getEstoqueOrigem());
        dto.setEstoqueDestino(entity.getEstoqueDestino());
        dto.setQuantidade(entity.getQuantidade());
        dto.setTipoMov(entity.getTipoMov());
        dto.setDataMovimentacao(entity.getDataMovimentacao());
        dto.setFuncionarioResponsavel(entity.getFuncionarioResponsavel());

        return dto;
    }

    // DTO → Entity
    public static MovEstoqueEntity toEntity(MovEstoqueDTO dto) {
        if (dto == null) return null;

        MovEstoqueEntity entity = new MovEstoqueEntity();
        entity.setInsumo(dto.getInsumo());
        entity.setEstoqueOrigem(dto.getEstoqueOrigem());
        entity.setEstoqueDestino(dto.getEstoqueDestino());
        entity.setQuantidade(dto.getQuantidade());
        entity.setTipoMov(dto.getTipoMov());
        entity.setDataMovimentacao(dto.getDataMovimentacao());
        entity.setFuncionarioResponsavel(dto.getFuncionarioResponsavel());

        return entity;
    }
}
