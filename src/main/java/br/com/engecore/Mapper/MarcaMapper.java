package br.com.engecore.Mapper;

import br.com.engecore.DTO.MarcaDTO;
import br.com.engecore.Entity.MarcaEntity;
import org.springframework.stereotype.Component;

@Component
public class MarcaMapper {

    // Entity -> DTO
    public static MarcaDTO toDTO(MarcaEntity entity) {
        if (entity == null) {
            return null;
        }
        MarcaDTO dto = new MarcaDTO();
        dto.setNome(entity.getNome());
        return dto;
    }

    // DTO -> Entity
    public static MarcaEntity toEntity(MarcaDTO dto) {
        if (dto == null) {
            return null;
        }
        MarcaEntity entity = new MarcaEntity();
        entity.setNome(dto.getNome());
        return entity;
    }
}