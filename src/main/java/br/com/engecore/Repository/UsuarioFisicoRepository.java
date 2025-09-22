package br.com.engecore.Repository;

import br.com.engecore.Entity.UsuarioFisico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioFisicoRepository extends JpaRepository<UsuarioFisico, Long> {
    UsuarioFisico findByUsuarioId(Long usuarioId);
}
