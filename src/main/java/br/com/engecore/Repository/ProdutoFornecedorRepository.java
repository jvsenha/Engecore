package br.com.engecore.Repository;

import br.com.engecore.Entity.FornecedorEntity;
import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Entity.ProdutoFornecedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoFornecedorRepository extends JpaRepository<ProdutoFornecedorEntity, Long> {
    // Busca todos os produtos de um fornecedor específico
    List<ProdutoFornecedorEntity> findByFornecedorId(Long fornecedorId);

    // Busca todos os fornecedores que oferecem um insumo específico
    List<ProdutoFornecedorEntity> findByInsumoId(Long insumoId);

    // Busca uma oferta específica de um insumo de um fornecedor
    Optional<ProdutoFornecedorEntity> findByInsumoAndFornecedor(InsumoEntity insumo, FornecedorEntity fornecedor);
}
