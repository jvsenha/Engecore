package br.com.engecore.Repository;

import br.com.engecore.Entity.FornecedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FornecedorRepository extends JpaRepository<FornecedorEntity, Long> {
}
