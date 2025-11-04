// Criar este arquivo
package br.com.engecore.Repository;

import br.com.engecore.Entity.CotacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CotacaoRepository extends JpaRepository<CotacaoEntity, Long> {
}