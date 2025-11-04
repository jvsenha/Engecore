package br.com.engecore.Mapper;

import br.com.engecore.DTO.PropostaCotacaoDTO;
import br.com.engecore.Entity.FornecedorEntity;
import br.com.engecore.Entity.PropostaCotacaoEntity;
import br.com.engecore.Entity.UsuarioFisico;
import br.com.engecore.Entity.UsuarioJuridico;
import br.com.engecore.Enum.TipoPessoa;
import br.com.engecore.Repository.UsuarioFisicoRepository;
import br.com.engecore.Repository.UsuarioJuridicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component // Importante: Torna este Mapper um Bean gerenciado pelo Spring
public class CotacaoMapper {

    // Injeta os repositórios necessários para buscar os dados de PF/PJ
    private final UsuarioJuridicoRepository usuarioJuridicoRepository;
    private final UsuarioFisicoRepository usuarioFisicoRepository;

    @Autowired
    public CotacaoMapper(UsuarioJuridicoRepository usuarioJuridicoRepository,
                         UsuarioFisicoRepository usuarioFisicoRepository) {
        this.usuarioJuridicoRepository = usuarioJuridicoRepository;
        this.usuarioFisicoRepository = usuarioFisicoRepository;
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

        // Garante que o produtoFornecedor e o fornecedor não são nulos
        if (proposta.getProdutoFornecedor() != null && proposta.getProdutoFornecedor().getFornecedor() != null) {
            FornecedorEntity fornecedor = proposta.getProdutoFornecedor().getFornecedor();

            // Usa os métodos auxiliares para buscar os dados corretos
            dto.setFornecedor(getNomeDoFornecedor(fornecedor));
            dto.setCnpj(getIdentificadorFornecedor(fornecedor)); // O campo no DTO chama "cnpj", mas armazena CPF ou CNPJ
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
        // Fallback para o nome base (Pessoa Física ou PJ sem nome fantasia)
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
}