package br.com.engecore.Repository;

import br.com.engecore.Entity.FuncionarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<FuncionarioEntity, Long> {


}
