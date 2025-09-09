package br.com.engecore.Repository;

import br.com.engecore.Entity.InsumoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsumoRepository extends JpaRepository<InsumoEntity, Long> {
}
