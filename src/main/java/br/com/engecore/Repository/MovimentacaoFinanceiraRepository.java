package br.com.engecore.Repository;

import br.com.engecore.Entity.MovimentacaoFinanceiraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoFinanceiraRepository extends JpaRepository<MovimentacaoFinanceiraEntity, Long> {
 List<MovimentacaoFinanceiraEntity> findByObra(Long id);
 List<MovimentacaoFinanceiraEntity> findByFuncionarioResponsavel(Long id);
}
