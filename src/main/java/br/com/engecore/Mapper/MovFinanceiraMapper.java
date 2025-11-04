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
        dto.setObraId(entity.getObra().getId());
        dto.setInsumoId(entity.getInsumo().getId());
        dto.setFuncionarioResponsavelId(entity.getFuncionarioResponsavel().getId());
        dto.setDataMovimento(entity.getDataMovimento());
        dto.setDescricao(entity.getDescricao());

        return dto;
    }

}
