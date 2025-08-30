package br.com.engecore.Repository;


import br.com.engecore.Entity.UserEntity;
import br.com.engecore.Enum.Role;
import br.com.engecore.Enum.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    List<UserEntity> findByRole(Role role);
    List<UserEntity> findByStatus(Status status);
}
