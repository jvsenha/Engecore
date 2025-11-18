package br.com.engecore.Mapper;

import br.com.engecore.DTO.MovFinanceiraDTO;
import br.com.engecore.Entity.MovFinanceiraEntity;
import org.springframework.stereotype.Component;

@Component
public class MovFinanceiraMapper {

    // Entity → DTO
    public static MovFinanceiraDTO toDTO(MovFinanceiraEntity entity) {
        if (entity == null) {
            return null;
        }

        MovFinanceiraDTO dto = new MovFinanceiraDTO();
        dto.setId(entity.getId());
        dto.setValor(entity.getValor());
        dto.setTipo(entity.getTipo());
        dto.setCategoriaFinanceira(entity.getCategoriaFinanceira());
        dto.setDataMovimento(entity.getDataMovimento());
        dto.setDescricao(entity.getDescricao());

        // --- CORREÇÃO AQUI ---
        // Verifica se getInsumo() não é nulo antes de chamar getId()
        if (entity.getInsumo() != null) {
            dto.setInsumoId(entity.getInsumo().getId());
        }
        // ---------------------

        if (entity.getFuncionarioResponsavel() != null) {
            dto.setFuncionarioResponsavelId(entity.getFuncionarioResponsavel().getId());
        }

        if (entity.getObra() != null) {
            dto.setObraId(entity.getObra().getId());
        }

        if (entity.getCliente() != null) {
            dto.setClienteId(entity.getCliente().getId());
        }

        return dto;
    }
}
