package br.com.engecore.Repository;

import br.com.engecore.Entity.ObrasEntity;
import br.com.engecore.Enum.ProgramaSocial;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObrasRepository extends JpaRepository<ObrasEntity, Long> {

    List<ObrasEntity> findByProgamaSocial(ProgramaSocial programaSocial);
    List<ObrasEntity> findByCliente(Long id);

    List<ObrasEntity> findAll(Specification<ObrasEntity> spec);

}
