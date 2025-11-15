package br.com.engecore.Mapper;

import br.com.engecore.DTO.CotacaoDetalhesDTO;
import br.com.engecore.DTO.CotacaoListDTO; // Importar novo DTO
import br.com.engecore.DTO.PropostaCotacaoDTO;
import br.com.engecore.Entity.*;
import br.com.engecore.Enum.TipoPessoa;
import br.com.engecore.Repository.UsuarioFisicoRepository;
import br.com.engecore.Repository.UsuarioJuridicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CotacaoMapper {

    private final UsuarioJuridicoRepository usuarioJuridicoRepository;
    private final UsuarioFisicoRepository usuarioFisicoRepository;

    @Autowired
    public CotacaoMapper(UsuarioJuridicoRepository usuarioJuridicoRepository,
                         UsuarioFisicoRepository usuarioFisicoRepository) {
        this.usuarioJuridicoRepository = usuarioJuridicoRepository;
        this.usuarioFisicoRepository = usuarioFisicoRepository;
    }

    /**
     * NOVO MÉTODO: Converte a Entity completa para o DTO "leve" de listagem.
     */
    public CotacaoListDTO toCotacaoListDTO(CotacaoEntity entity) {
        if (entity == null) return null;

        CotacaoListDTO dto = new CotacaoListDTO();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setPrioridade(entity.getPrioridade());
        dto.setDataNecessidade(entity.getDataNecessidade());
        dto.setQuantidade(entity.getQuantidade());

        // Mapeia nomes de entidades relacionadas (evita aninhamento)
        if (entity.getInsumo() != null) {
            dto.setNomeInsumo(entity.getInsumo().getNome());
        }
        if (entity.getObra() != null) {
            dto.setNomeObra(entity.getObra().getNomeObra());
        }

        // Processa as propostas para encontrar a melhor
        if (entity.getPropostas() != null && !entity.getPropostas().isEmpty()) {
            dto.setTotalPropostas(entity.getPropostas().size());

            // Encontra a proposta marcada como a melhor
            PropostaCotacaoEntity melhorProposta = entity.getPropostas().stream()
                    .filter(PropostaCotacaoEntity::isMelhorPreco)
                    .findFirst()
                    .orElse(null); // Se não houver, tenta pegar a primeira

            if (melhorProposta == null) {
                melhorProposta = entity.getPropostas().get(0);
            }

            dto.setMelhorValorUnitario(melhorProposta.getValorUnitario());

            // Pega o nome do fornecedor da melhor proposta
            if (melhorProposta.getProdutoFornecedor() != null && melhorProposta.getProdutoFornecedor().getFornecedor() != null) {
                // Reutiliza a lógica do método privado getNomeDoFornecedor
                dto.setNomeMelhorFornecedor(
                        getNomeDoFornecedor(melhorProposta.getProdutoFornecedor().getFornecedor())
                );
            } else {
                dto.setNomeMelhorFornecedor("N/A");
            }

        } else {
            // Se não há propostas
            dto.setTotalPropostas(0);
            dto.setMelhorValorUnitario(BigDecimal.ZERO);
            dto.setNomeMelhorFornecedor("Sem propostas");
        }

        return dto;
    }


    /**
     * Converte uma PropostaCotacaoEntity para o DTO que o frontend espera.
     */
    public PropostaCotacaoDTO toPropostaDTO(PropostaCotacaoEntity proposta) {
        if (proposta == null) {
            return null;
        }

        PropostaCotacaoDTO dto = new PropostaCotacaoDTO();
        dto.setId(proposta.getId());

        if (proposta.getProdutoFornecedor() != null && proposta.getProdutoFornecedor().getFornecedor() != null) {
            FornecedorEntity fornecedor = proposta.getProdutoFornecedor().getFornecedor();
            dto.setFornecedor(getNomeDoFornecedor(fornecedor));
            dto.setCnpj(getIdentificadorFornecedor(fornecedor));
        } else {
            dto.setFornecedor("Fornecedor não informado");
            dto.setCnpj("N/A");
        }

        dto.setValorUnitario(proposta.getValorUnitario());
        dto.setPrazoEntrega(proposta.getPrazoEntrega());
        dto.setCondicaoPagamento(proposta.getCondicaoPagamento());
        dto.setObservacoes(proposta.getObservacoes());
        dto.setMelhorPreco(proposta.isMelhorPreco());

        return dto;
    }

    /**
     * Busca o nome fantasia (se PJ) ou o nome (se PF) do fornecedor.
     */
    private String getNomeDoFornecedor(FornecedorEntity fornecedor) {
        if (fornecedor == null) return "Fornecedor N/A";

        if (fornecedor.getTipoPessoa() == TipoPessoa.JURIDICA) {
            UsuarioJuridico uj = usuarioJuridicoRepository.findByUsuarioId(fornecedor.getId());
            if (uj != null && uj.getNomeFantasia() != null && !uj.getNomeFantasia().isEmpty()) {
                return uj.getNomeFantasia();
            }
        }
        return fornecedor.getNome();
    }

    /**
     * Busca o CNPJ (se PJ) ou o CPF (se PF) do fornecedor.
     */
    private String getIdentificadorFornecedor(FornecedorEntity fornecedor) {
        if (fornecedor == null) return "ID N/A";

        if (fornecedor.getTipoPessoa() == TipoPessoa.JURIDICA) {
            UsuarioJuridico uj = usuarioJuridicoRepository.findByUsuarioId(fornecedor.getId());
            return (uj != null) ? uj.getCnpj() : "CNPJ N/A";
        } else if (fornecedor.getTipoPessoa() == TipoPessoa.FISICA) {
            UsuarioFisico uf = usuarioFisicoRepository.findByUsuarioId(fornecedor.getId());
            return (uf != null) ? uf.getCpf() : "CPF N/A";
        }
        return "N/A";
    }

    public CotacaoDetalhesDTO toDetalhesDTO(CotacaoEntity entity) {
        if (entity == null) return null;

        CotacaoDetalhesDTO dto = new CotacaoDetalhesDTO();
        dto.setId(entity.getId());
        dto.setInsumo(entity.getInsumo());
        dto.setQuantidade(entity.getQuantidade());
        dto.setDataNecessidade(entity.getDataNecessidade());
        dto.setPrioridade(entity.getPrioridade());
        dto.setStatus(entity.getStatus());
        dto.setPropostas(entity.getPropostas());

        // Mapeia apenas os nomes de entidades relacionadas
        if (entity.getObra() != null) {
            dto.setNomeObra(entity.getObra().getNomeObra());
        }
        if (entity.getFuncionarioSolicitante() != null) {
            dto.setFuncionarioSolicitante(entity.getFuncionarioSolicitante().getNome());
        }

        return dto;
    }
}