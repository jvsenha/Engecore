package br.com.engecore.Repository;

import br.com.engecore.Entity.UsuarioJuridico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioJuridicoRepository extends JpaRepository<UsuarioJuridico, Long> {
    UsuarioJuridico findByUsuarioId(Long usuarioId);
}
