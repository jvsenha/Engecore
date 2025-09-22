package br.com.engecore.Repository;

import br.com.engecore.Entity.MovEstoqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovEstoqueRepository extends JpaRepository<MovEstoqueEntity, Long> {
    List<MovEstoqueEntity> findByFuncionarioResponsavelId(Long id);
}
