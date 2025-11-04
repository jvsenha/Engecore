// Criar este arquivo
package br.com.engecore.Repository;

import br.com.engecore.Entity.PropostaCotacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PropostaCotacaoRepository extends JpaRepository<PropostaCotacaoEntity, Long> {
    List<PropostaCotacaoEntity> findByCotacaoId(Long cotacaoId);
}