package br.com.engecore.Repository;

import br.com.engecore.Entity.ObrasEntity;
import br.com.engecore.Enum.ProgramaSocial;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObrasRepository extends JpaRepository<ObrasEntity, Long> {

    // Buscar por programa social
    List<ObrasEntity> findByProgramaSocial(ProgramaSocial programaSocial);

    // Buscar por cliente usando o id correto da superclasse UserEntity
    List<ObrasEntity> findByClienteIdUsuario(Long idUsuario);

    // Buscar com specification
    List<ObrasEntity> findAll(Specification<ObrasEntity> spec);
}
