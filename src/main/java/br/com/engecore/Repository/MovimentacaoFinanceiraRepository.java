package br.com.engecore.Repository;

import br.com.engecore.Entity.MovimentacaoFinanceiraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentacaoFinanceiraRepository extends JpaRepository<MovimentacaoFinanceiraEntity, Long> {

 // Buscar por ID da obra
 List<MovimentacaoFinanceiraEntity> findByObraIdObra(Long idObra);

 // Buscar por ID do funcionário responsável
 List<MovimentacaoFinanceiraEntity> findByFuncionarioResponsavelIdUsuario(Long idUsuario);
}
