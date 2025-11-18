package br.com.engecore.Repository;

import br.com.engecore.DTO.EstoqueDTO;
import br.com.engecore.Entity.EstoqueEntity;
import br.com.engecore.Entity.ObrasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<EstoqueEntity,Long> {

    EstoqueEntity findByObra(ObrasEntity obra);
}
