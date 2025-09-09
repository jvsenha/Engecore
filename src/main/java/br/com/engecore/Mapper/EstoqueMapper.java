package br.com.engecore.Mapper;

import br.com.engecore.DTO.EstoqueDTO;
import br.com.engecore.Entity.EstoqueEntity;

public class EstoqueMapper {


        // Entity → DTO
        public static EstoqueDTO toDTO(EstoqueEntity entity) {
            if (entity == null) return null;

            EstoqueDTO dto = new EstoqueDTO();
            dto.setNome(entity.getNome());
            dto.setTipo(entity.getTipo());
            dto.setEstoqueMateriais(entity.getEstoqueMateriais()); // já retorna a lista de EstoqueMaterial

            return dto;
        }

        // DTO → Entity
        public static EstoqueEntity toEntity(EstoqueDTO dto) {
            if (dto == null) return null;

            EstoqueEntity entity = new EstoqueEntity();
            entity.setNome(dto.getNome());
            entity.setTipo(dto.getTipo());
            entity.setEstoqueMateriais(dto.getEstoqueMateriais()); // mantém a lista

            return entity;
        }
    }


