package br.com.engecore.Repository;

import br.com.engecore.Entity.EstoqueEntity;
import br.com.engecore.Entity.InsumoEntity;
import br.com.engecore.Entity.MaterialEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaterialEstoqueRepository extends JpaRepository<MaterialEstoque, Long> {
    List<MaterialEstoque> findByEstoqueId(Long estoqueId);
    Optional<MaterialEstoque> findByEstoqueAndMaterial(EstoqueEntity estoque, InsumoEntity material);
}
