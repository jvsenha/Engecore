package br.com.engecore.Repository;

import br.com.engecore.Entity.MaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository  extends JpaRepository<MaterialEntity, Long> {
}
