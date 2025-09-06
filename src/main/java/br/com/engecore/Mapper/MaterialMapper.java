package br.com.engecore.Mapper;

import br.com.engecore.DTO.MaterialDTO;
import br.com.engecore.Entity.MaterialEntity;

public class MaterialMapper {
    public static MaterialEntity toEntity(MaterialDTO dto) {
        if (dto == null) {
            return null;
        }

        MaterialEntity entity = new MaterialEntity();
        entity.setNome(dto.getNome());
        entity.setUnidade(dto.getUnidade());
        return entity;
    }

    // Entity -> DTO
    public static MaterialDTO toDTO(MaterialEntity entity) {
        if (entity == null) {
            return null;
        }

        MaterialDTO dto = new MaterialDTO();
        dto.setNome(entity.getNome());
        dto.setUnidade(entity.getUnidade());
        return dto;
    }
}
