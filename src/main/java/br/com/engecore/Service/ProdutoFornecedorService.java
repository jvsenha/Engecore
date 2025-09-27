package br.com.engecore.Service;

import br.com.engecore.Entity.FornecedorEntity;
import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Entity.ProdutoFornecedorEntity;
import br.com.engecore.Repository.FornecedorRepository;
import br.com.engecore.Repository.InsumoRepository;
import br.com.engecore.Repository.ProdutoFornecedorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;
import java.util.Optional;

public class ProdutoFornecedorService {

    @Autowired
    private ProdutoFornecedorRepository produtoFornecedorRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    @Transactional
    @PreAuthorize("@securityService.isFornecedor(authentication)")
    public ProdutoFornecedorEntity cadastrarOuAtualizar(Long fornecedorId, Long insumoId, double valor) {
        FornecedorEntity fornecedor = fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        InsumoEntity insumo = insumoRepository.findById(insumoId)
                .orElseThrow(() -> new RuntimeException("Insumo não encontrado"));

        Optional<ProdutoFornecedorEntity> existing = produtoFornecedorRepository.findByInsumoAndFornecedor(insumo, fornecedor);

        ProdutoFornecedorEntity produtoFornecedor;
        if (existing.isPresent()) {
            produtoFornecedor = existing.get();
        } else {
            produtoFornecedor = new ProdutoFornecedorEntity();
            produtoFornecedor.setFornecedor(fornecedor);
            produtoFornecedor.setInsumo(insumo);
        }

        produtoFornecedor.setValor(new java.math.BigDecimal(valor));
        produtoFornecedor.setDataAtualizacao(LocalDate.now());

        return produtoFornecedorRepository.save(produtoFornecedor);
    }
}
