package br.com.engecore.Repository;

import br.com.engecore.Entity.MovFinanceiraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovFinanceiraRepository extends JpaRepository<MovFinanceiraEntity, Long> {

 // Buscar por ID da obra
 List<MovFinanceiraEntity> findByObraIdObra(Long idObra);

 // Buscar por ID do funcionário responsável
 List<MovFinanceiraEntity> findByFuncionarioResponsavelIdUsuario(Long idUsuario);
}
