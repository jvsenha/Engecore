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
import java.util.stream.Collectors; // Importar Collectors

@Service
public class ProdutoFornecedorService {

    @Autowired
    private ProdutoFornecedorRepository produtoFornecedorRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Autowired
    private MarcaRepository marcaRepository;

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

        MarcaEntity marca = marcaRepository.findById(dto.getMarcaId())
                .orElseThrow(() -> new RuntimeException("Marca não encontrada"));

        // Procura se o fornecedor já vende este insumo (com este modelo e marca)
        Optional<ProdutoFornecedorEntity> existing = produtoFornecedorRepository.findByInsumoAndFornecedorAndMarcaAndModelo(insumo, fornecedor, marca, dto.getModelo());

        ProdutoFornecedorEntity produtoFornecedor;
        if (existing.isPresent()) {
            // Atualiza o produto existente
            produtoFornecedor = existing.get();
        } else {
            // Cria um novo produto
            produtoFornecedor = new ProdutoFornecedorEntity();
            produtoFornecedor.setFornecedor(fornecedor);
            produtoFornecedor.setInsumo(insumo);
            produtoFornecedor.setMarca(marca);
            produtoFornecedor.setModelo(dto.getModelo());
        }

        // Atualiza os campos
        produtoFornecedor.setValor(dto.getValor());
        produtoFornecedor.setPrazoEntrega(dto.getPrazoEntrega());
        produtoFornecedor.setCondicaoPagamento(dto.getCondicaoPagamento());
        produtoFornecedor.setObservacoes(dto.getObservacoes());
        produtoFornecedor.setDataAtualizacao(LocalDate.now());

        return produtoFornecedorRepository.save(produtoFornecedor);
    }

    /**
     * Endpoint para listar todos os produtos de um fornecedor específico.
     * AGORA RETORNA UM DTO COM OS NOMES.
     */
    @PreAuthorize("@securityService.isAdmin(authentication) or @securityService.isFuncionario(authentication) or @securityService.isFornecedor(authentication)")
    public List<ProdutoFornecedorDTO> listarProdutosPorFornecedor(Long fornecedorId) {
        // 1. Busca as Entidades do repositório
        List<ProdutoFornecedorEntity> entidades = produtoFornecedorRepository.findByFornecedorId(fornecedorId);

        // 2. Converte cada Entidade para DTO usando o helper
        return entidades.stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Método auxiliar privado para converter a Entidade em DTO.
     */
    private ProdutoFornecedorDTO mapEntityToDTO(ProdutoFornecedorEntity entity) {
        if (entity == null) {
            return null;
        }

        ProdutoFornecedorDTO dto = new ProdutoFornecedorDTO();

        // ID da própria relação (útil para o frontend)
        dto.setId(entity.getId());

        dto.setModelo(entity.getModelo());
        dto.setValor(entity.getValor());
        dto.setPrazoEntrega(entity.getPrazoEntrega());
        dto.setCondicaoPagamento(entity.getCondicaoPagamento());
        dto.setObservacoes(entity.getObservacoes());

        // IDs E NOMES (A CORREÇÃO)
        if (entity.getInsumo() != null) {
            dto.setInsumoId(entity.getInsumo().getId());
            dto.setInsumoNome(entity.getInsumo().getNome()); // <-- Nome do Insumo
        }

        if (entity.getFornecedor() != null) {
            dto.setFornecedorId(entity.getFornecedor().getId());
        }

        if (entity.getMarca() != null) {
            dto.setMarcaId(entity.getMarca().getId());
            dto.setMarcaNome(entity.getMarca().getNome()); // <-- Nome da Marca
        }

        return dto;
    }
}