package br.com.engecore.Repository;

import br.com.engecore.Entity.EstoqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<EstoqueEntity,Long> {
}
