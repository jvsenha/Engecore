package br.com.engecore.Mapper;

import br.com.engecore.DTO.InsumoDTO;
import br.com.engecore.Entity.InsumoEntity;
import org.springframework.stereotype.Component;

@Component
public class InsumoMapper {
    public static InsumoEntity toEntity(InsumoDTO dto) {
        if (dto == null) {
            return null;
        }

        InsumoEntity entity = new InsumoEntity();
        entity.setNome(dto.getNome());
        entity.setModelo(dto.getModelo());
        entity.setMarca(dto.getMarca());
        entity.setUnidade(dto.getUnidade());
        return entity;
    }

    // Entity -> DTO
    public static InsumoDTO toDTO(InsumoEntity entity) {
        if (entity == null) {
            return null;
        }

        InsumoDTO dto = new InsumoDTO();
        dto.setNome(entity.getNome());
        dto.setModelo(entity.getModelo());
        dto.setMarca(entity.getMarca());
        dto.setUnidade(entity.getUnidade());
        return dto;
    }
}
