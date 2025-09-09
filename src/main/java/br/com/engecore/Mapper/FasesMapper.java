package br.com.engecore.Mapper;

import br.com.engecore.DTO.FasesDTO;
import br.com.engecore.Entity.FasesEntity;

public class FasesMapper {
    // Entity → DTO
    public static FasesDTO toDTO(FasesEntity entity) {
        if (entity == null) return null;

        FasesDTO dto = new FasesDTO();
        dto.setNome(entity.getNome());
        dto.setDataInicio(entity.getDataInicio());
        dto.setDataTermino(entity.getDataTermino());
        dto.setTempoEsperado(entity.getTempoEsperado());
        dto.setTempoLevado(entity.getTempoLevado());
        dto.setDescricao(entity.getDescricao());

        return dto;
    }

    // DTO → Entity
    public static FasesEntity toEntity(FasesDTO dto) {
        if (dto == null) return null;

        FasesEntity entity = new FasesEntity();
        entity.setNome(dto.getNome());
        entity.setDataInicio(dto.getDataInicio());
        entity.setDataTermino(dto.getDataTermino());
        entity.setTempoEsperado(dto.getTempoEsperado());
        entity.setTempoLevado(dto.getTempoLevado());
        entity.setDescricao(dto.getDescricao());

        return entity;
    }
}
