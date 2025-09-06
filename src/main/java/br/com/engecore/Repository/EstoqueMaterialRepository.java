package br.com.engecore.Repository;

import br.com.engecore.Entity.EstoqueMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstoqueMaterialRepository extends JpaRepository<EstoqueMaterial, Long> {
    Optional<EstoqueMaterial> findByEstoque_IdAndMaterial_Id(Long estoqueId, Long materialId);

}
