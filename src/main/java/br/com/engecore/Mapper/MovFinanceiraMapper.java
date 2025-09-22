package br.com.engecore.Mapper;

import br.com.engecore.DTO.MovFinanceiraDTO;
import br.com.engecore.Entity.MovFinanceiraEntity;
import org.springframework.stereotype.Component;

@Component
public class MovFinanceiraMapper {

    // Entity → DTO
    public static MovFinanceiraDTO toDTO(MovFinanceiraEntity entity) {
        if (entity == null) return null;

        MovFinanceiraDTO dto = new MovFinanceiraDTO();
        dto.setValor(entity.getValor());
        dto.setTipo(entity.getTipo());
        dto.setCategoriaFinanceira(entity.getCategoriaFinanceira());
        dto.setObra(entity.getObra());
        dto.setInsumo(entity.getInsumo());
        dto.setFuncionarioResponsavel(entity.getFuncionarioResponsavel());
        dto.setDataMovimento(entity.getDataMovimento());
        dto.setDescricao(entity.getDescricao());

        return dto;
    }

    // DTO → Entity
    public static MovFinanceiraEntity toEntity(MovFinanceiraDTO dto) {
        if (dto == null) return null;

        MovFinanceiraEntity entity = new MovFinanceiraEntity();
        entity.setValor(dto.getValor());
        entity.setTipo(dto.getTipo());
        entity.setCategoriaFinanceira(dto.getCategoriaFinanceira());
        entity.setObra(dto.getObra());
        entity.setInsumo(dto.getInsumo());
        entity.setFuncionarioResponsavel(dto.getFuncionarioResponsavel());
        entity.setDataMovimento(dto.getDataMovimento());
        entity.setDescricao(dto.getDescricao());

        return entity;
    }
}
