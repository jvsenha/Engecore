package br.com.engecore.Service;

import br.com.engecore.DTO.ProdutoFornecedorDTO;
import br.com.engecore.Entity.FornecedorEntity;
import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Entity.MarcaEntity;
import br.com.engecore.Entity.ProdutoFornecedorEntity;
import br.com.engecore.Repository.FornecedorRepository;
import br.com.engecore.Repository.InsumoRepository;
import br.com.engecore.Repository.MarcaRepository; // Importa o Repository
import br.com.engecore.Repository.ProdutoFornecedorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoFornecedorService {

    @Autowired
    private ProdutoFornecedorRepository produtoFornecedorRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    // Você precisará criar este repositório
    @Autowired
    private MarcaRepository marcaRepository; // DESCOMENTADO

    /**
     * Cadastra ou atualiza um produto no catálogo de um fornecedor.
     * Somente o próprio fornecedor (logado) ou um ADM/Funcionário pode fazer isso.
     */
    @Transactional
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication) or @securityService.isFornecedor(authentication)")
    public ProdutoFornecedorEntity cadastrarOuAtualizar(ProdutoFornecedorDTO dto) {

        FornecedorEntity fornecedor = fornecedorRepository.findById(dto.getFornecedorId())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        InsumoEntity insumo = insumoRepository.findById(dto.getInsumoId())
                .orElseThrow(() -> new RuntimeException("Insumo não encontrado"));

        // MARCA - Agora está funcional
        MarcaEntity marca = marcaRepository.findById(dto.getMarcaId())
                .orElseThrow(() -> new RuntimeException("Marca não encontrada"));

        // Procura se o fornecedor já vende este insumo (com este modelo e marca)
        // A lógica de "findByInsumoAndFornecedor" talvez precise ser mais específica
        Optional<ProdutoFornecedorEntity> existing = produtoFornecedorRepository.findByInsumoAndFornecedor(insumo, fornecedor);

        ProdutoFornecedorEntity produtoFornecedor;
        if (existing.isPresent()) {
            // Atualiza o produto existente
            produtoFornecedor = existing.get();
        } else {
            // Cria um novo produto
            produtoFornecedor = new ProdutoFornecedorEntity();
            produtoFornecedor.setFornecedor(fornecedor);
            produtoFornecedor.setInsumo(insumo);
        }

        // Atualiza todos os campos
        produtoFornecedor.setValor(dto.getValor());
        produtoFornecedor.setModelo(dto.getModelo());
        produtoFornecedor.setMarca(marca); // DESCOMENTADO
        produtoFornecedor.setPrazoEntrega(dto.getPrazoEntrega());
        produtoFornecedor.setCondicaoPagamento(dto.getCondicaoPagamento());
        produtoFornecedor.setObservacoes(dto.getObservacoes());
        produtoFornecedor.setDataAtualizacao(LocalDate.now());

        return produtoFornecedorRepository.save(produtoFornecedor);
    }

    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication) or @securityService.isFornecedor(authentication)")
    public List<ProdutoFornecedorEntity> listarProdutosPorFornecedor(Long fornecedorId) {
        return produtoFornecedorRepository.findByFornecedorId(fornecedorId);
    }
}