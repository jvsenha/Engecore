package br.com.engecore.Mapper;

import br.com.engecore.DTO.ObrasDTO;
import br.com.engecore.Entity.ClienteEntity;
import br.com.engecore.Entity.EnderecoEmbeddable;
import br.com.engecore.Entity.FuncionarioEntity;
import br.com.engecore.Entity.ObrasEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;

@Component
public class ObrasMapper {

    // Entity → DTO
    public static ObrasDTO toDTO(ObrasEntity entity) {
        if (entity == null) return null;

        ObrasDTO dto = new ObrasDTO();

        dto.setIdObra(entity.getId());
        dto.setNomeObra(entity.getNomeObra());

        // Endereço com valores padrão
        EnderecoEmbeddable e = entity.getEndereco() != null ? entity.getEndereco() : new EnderecoEmbeddable();
        e.setRua(e.getRua() != null ? e.getRua() : "");
        e.setNumero(e.getNumero() != null ? e.getNumero() : "");
        e.setComplemento(e.getComplemento() != null ? e.getComplemento() : "");
        e.setBairro(e.getBairro() != null ? e.getBairro() : "");
        e.setCidade(e.getCidade() != null ? e.getCidade() : "");
        e.setEstado(e.getEstado() != null ? e.getEstado() : "");
        e.setCep(e.getCep() != null ? e.getCep() : "");
        dto.setEndereco(e);

        dto.setStatus(entity.getStatusConst());
        dto.setTipo(entity.getTipo());

        // Cliente
        if (entity.getCliente() != null) {
            dto.setClienteId(entity.getCliente().getId());
            dto.setClienteNome(entity.getCliente().getNome() != null ? entity.getCliente().getNome() : "");
        }

        // Responsável
        if (entity.getResponsavel() != null) {
            dto.setResponsavelId(entity.getResponsavel().getId());
            dto.setResponsavelNome(entity.getResponsavel().getNome() != null ? entity.getResponsavel().getNome() : "");
        }

        // Unidades
        dto.setTotalUnidades(entity.getTotalUnidades() != null ? entity.getTotalUnidades() : 0);
        dto.setUnidadesConcluidas(entity.getUnidadesConcluidas() != null ? entity.getUnidadesConcluidas() : 0);

        // Financeiro
        dto.setValorTotal(entity.getValorTotal() != null ? entity.getValorTotal() : BigDecimal.ZERO);
        dto.setValorLiberado(entity.getValorLiberado() != null ? entity.getValorLiberado() : BigDecimal.ZERO);
        dto.setPagosFornecedores(entity.getPagosFornecedores() != null ? entity.getPagosFornecedores() : BigDecimal.ZERO);
        dto.setCustoPorUnidade(entity.getCustoPorUnidade() != null ? entity.getCustoPorUnidade() : BigDecimal.ZERO);

        // Programas sociais
        dto.setFaixaRenda(entity.getFaixaRenda());
        dto.setDocumentacaoAprovada(entity.getDocumentacaoAprovada() != null ? entity.getDocumentacaoAprovada() : false);
        dto.setProgramaSocial(entity.getProgramaSocial());

        // Datas
        dto.setDataInicio(entity.getDataInicio());
        dto.setDataPrevistaConclusao(entity.getDataPrevistaConclusao());
        dto.setDataConclusaoReal(entity.getDataConclusaoReal());

        // Fases
        dto.setFases(FasesMapper.toDTOList(entity.getFases() != null ? entity.getFases() : new ArrayList<>()));


        return dto;
    }

    // DTO → Entity
    public static ObrasEntity toEntity(ObrasDTO dto) {
        if (dto == null) return null;

        ObrasEntity entity = new ObrasEntity();

        entity.setId(dto.getIdObra());
        entity.setNomeObra(dto.getNomeObra());
        entity.setEndereco(dto.getEndereco() != null ? dto.getEndereco() : new EnderecoEmbeddable());

        entity.setStatusConst(dto.getStatus());
        entity.setTipo(dto.getTipo());

        // Cliente
        if (dto.getClienteId() != null) {
            ClienteEntity cliente = new ClienteEntity();
            cliente.setId(dto.getClienteId());
            entity.setCliente(cliente);
        }

        // Responsável
        if (dto.getResponsavelId() != null) {
            FuncionarioEntity funcionario = new FuncionarioEntity();
            funcionario.setId(dto.getResponsavelId());
            entity.setResponsavel(funcionario);
        }

        // Unidades
        entity.setTotalUnidades(dto.getTotalUnidades() != null ? dto.getTotalUnidades() : 0);
        entity.setUnidadesConcluidas(dto.getUnidadesConcluidas() != null ? dto.getUnidadesConcluidas() : 0);

        // Financeiro
        entity.setValorTotal(dto.getValorTotal() != null ? dto.getValorTotal() : BigDecimal.ZERO);
        entity.setValorLiberado(dto.getValorLiberado() != null ? dto.getValorLiberado() : BigDecimal.ZERO);
        entity.setPagosFornecedores(dto.getPagosFornecedores() != null ? dto.getPagosFornecedores() : BigDecimal.ZERO);
        entity.setCustoPorUnidade(dto.getCustoPorUnidade() != null ? dto.getCustoPorUnidade() : BigDecimal.ZERO);

        // Programas sociais
        entity.setFaixaRenda(dto.getFaixaRenda());
        entity.setDocumentacaoAprovada(dto.getDocumentacaoAprovada() != null ? dto.getDocumentacaoAprovada() : false);
        entity.setProgramaSocial(dto.getProgramaSocial());

        // Datas
        entity.setDataInicio(dto.getDataInicio());
        entity.setDataPrevistaConclusao(dto.getDataPrevistaConclusao());
        entity.setDataConclusaoReal(dto.getDataConclusaoReal());

        // Fases
        entity.setFases(FasesMapper.toEntityList(dto.getFases(), entity));

        return entity;
    }
}
