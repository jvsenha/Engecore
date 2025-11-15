package br.com.engecore.Repository;

import br.com.engecore.Entity.MarcaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaRepository extends JpaRepository<MarcaEntity, Long> {
}
