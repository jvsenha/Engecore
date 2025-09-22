package br.com.engecore.Mapper;

import br.com.engecore.DTO.FasesDTO;
import br.com.engecore.Entity.FasesEntity;
import br.com.engecore.Entity.ObrasEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FasesMapper {

    // Entity → DTO
    public static FasesDTO toDTO(FasesEntity entity) {
        if (entity == null) return null;

        FasesDTO dto = new FasesDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setObra(entity.getObra() != null ? entity.getObra().getId() : null);
        dto.setDataInicio(entity.getDataInicio());
        dto.setDataTermino(entity.getDataTermino());
        dto.setTempoEsperado(entity.getTempoEsperado() != null ? entity.getTempoEsperado() : 0);
        dto.setTempoLevado(entity.getTempoLevado() != null ? entity.getTempoLevado() : 0);
        dto.setDescricao(entity.getDescricao() != null ? entity.getDescricao() : "");

        return dto;
    }

    // DTO → Entity
    public static FasesEntity toEntity(FasesDTO dto, ObrasEntity obra) {
        if (dto == null) return null;

        FasesEntity entity = new FasesEntity();
        entity.setNome(dto.getNome() != null ? dto.getNome() : "");
        entity.setObra(obra);
        entity.setDataInicio(dto.getDataInicio());
        entity.setDataTermino(dto.getDataTermino());
        entity.setTempoEsperado(dto.getTempoEsperado());
        entity.setTempoLevado(dto.getTempoLevado());
        entity.setDescricao(dto.getDescricao() != null ? dto.getDescricao() : "");

        return entity;
    }

    public static List<FasesDTO> toDTOList(List<FasesEntity> entities) {
        if (entities == null) return new ArrayList<>();
        return entities.stream().map(FasesMapper::toDTO).collect(Collectors.toList());
    }

    public static List<FasesEntity> toEntityList(List<FasesDTO> dtos, ObrasEntity obra) {
        if (dtos == null) return new ArrayList<>();
        return dtos.stream().map(dto -> toEntity(dto, obra)).collect(Collectors.toList());
    }
}
