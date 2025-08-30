package br.com.engecore.Mapper;

import br.com.engecore.DTO.ObrasDTO;
import br.com.engecore.Entity.ClienteEntity;
import br.com.engecore.Entity.FuncionarioEntity;
import br.com.engecore.Entity.ObrasEntity;

import java.math.BigDecimal;

public class ObrasMapper {

        // Converte de Entity para DTO
        public static ObrasDTO toDTO(ObrasEntity entity) {
            if (entity == null) {
                return null;
            }

            ObrasDTO dto = new ObrasDTO();

            dto.setIdObra(entity.getIdObra());
            dto.setNomeObra(entity.getNomeObra());

            // Endereço como String simplificada
            dto.setEndereco(entity.getEndereco() != null ? entity.getEndereco() : null);

            dto.setStatus(entity.getStatusConst());
            dto.setTipo(entity.getTipo());

            // Cliente
            if (entity.getCliente() != null) {
                dto.setClienteId(entity.getCliente().getIdUsuario());
                dto.setClienteNome(entity.getCliente().getNome());
            }

            // Responsável
            if (entity.getResponsavel() != null) {
                dto.setResponsavelId(entity.getResponsavel().getIdUsuario());
                dto.setResponsavelNome(entity.getResponsavel().getNome());
            }

            // Dados unidades
            dto.setTotalUnidades(entity.getTotalUnidades());
            dto.setUnidadesConcluidas(entity.getUnidadesConcluidas());

            // Financeiro
            dto.setValorTotal(entity.getValorTotal() != null ? entity.getValorTotal().doubleValue() : null);
            dto.setValorLiberado(entity.getValorLiberado() != null ? entity.getValorLiberado().doubleValue() : null);
            dto.setPagosFornecedores(entity.getPagosFornecedores() != null ? entity.getPagosFornecedores().doubleValue() : null);
            dto.setCustoPorUnidade(entity.getCustoPorUnidade() != null ? entity.getCustoPorUnidade().doubleValue() : null);

            // Programas sociais
            dto.setFaixaRenda(entity.getFaixaRenda());
            dto.setDocumentacaoAprovada(entity.getDocumentacaoAprovada());
            dto.setProgramaSocial(entity.getProgramaSocial());

            // Datas
            dto.setDataInicio(entity.getDataInicio());
            dto.setDataPrevistaConclusao(entity.getDataPrevistaConclusao());
            dto.setDataConclusaoReal(entity.getDataConclusaoReal());

            // Fases
            dto.setFases(entity.getFases());

            return dto;
        }

        // Converte de DTO para Entity
        public static ObrasEntity toEntity(ObrasDTO dto) {
            if (dto == null) {
                return null;
            }

            ObrasEntity entity = new ObrasEntity();

            entity.setIdObra(dto.getIdObra());
            entity.setNomeObra(dto.getNomeObra());

            entity.setEndereco(dto.getEndereco());


            entity.setStatusConst(dto.getStatus());
            entity.setTipo(dto.getTipo());

            // Cliente
            if (dto.getClienteId() != null) {
                ClienteEntity cliente = new ClienteEntity();
                cliente.setIdUsuario(dto.getClienteId());
                entity.setCliente(cliente);
            }

            // Responsável
            if (dto.getResponsavelId() != null) {
                FuncionarioEntity funcionario = new FuncionarioEntity();
                funcionario.setIdUsuario(dto.getResponsavelId());
                entity.setResponsavel(funcionario);
            }

            // Dados unidades
            entity.setTotalUnidades(dto.getTotalUnidades());
            entity.setUnidadesConcluidas(dto.getUnidadesConcluidas());

            // Financeiro
            entity.setValorTotal(dto.getValorTotal() != null ? BigDecimal.valueOf(dto.getValorTotal()) : null);
            entity.setValorLiberado(dto.getValorLiberado() != null ? BigDecimal.valueOf(dto.getValorLiberado()) : null);
            entity.setPagosFornecedores(dto.getPagosFornecedores() != null ? BigDecimal.valueOf(dto.getPagosFornecedores()) : null);
            entity.setCustoPorUnidade(dto.getCustoPorUnidade() != null ? BigDecimal.valueOf(dto.getCustoPorUnidade()) : null);

            // Programas sociais
            entity.setFaixaRenda(dto.getFaixaRenda());
            entity.setDocumentacaoAprovada(dto.getDocumentacaoAprovada());
            entity.setProgramaSocial(dto.getProgramaSocial());
            // Datas
            entity.setDataInicio(dto.getDataInicio());
            entity.setDataPrevistaConclusao(dto.getDataPrevistaConclusao());
            entity.setDataConclusaoReal(dto.getDataConclusaoReal());

            // Fases
            entity.setFases(dto.getFases());

            return entity;
        }


}
