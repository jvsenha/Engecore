package br.com.engecore.Repository;

import br.com.engecore.Entity.FasesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FasesRepository extends JpaRepository<FasesEntity, Long> {

   List<FasesEntity> findByObraId(Long id);
}
