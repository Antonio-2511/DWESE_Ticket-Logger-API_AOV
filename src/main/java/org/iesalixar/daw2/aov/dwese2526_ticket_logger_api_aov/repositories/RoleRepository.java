package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories;

import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Recupera los roles cuyos ids estén en el conjunto indicado.
     */
    List<Role> findByIdIn(Set<Long> ids);

    Optional<Role> findByName(String name);
}
