package br.com.engecore.Repository;

import br.com.engecore.Entity.MovimentacaoEstoqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoqueEntity, Long> {
    List<MovimentacaoEstoqueEntity> findByFuncionarioResponsavel(Long id);
}
