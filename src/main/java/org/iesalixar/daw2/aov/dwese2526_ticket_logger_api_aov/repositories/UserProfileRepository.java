package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories;

import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para la entidad {@link UserProfile}.
 *
 * Pensado para la gestión del perfil de usuario ("Mi perfil").
 */
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    /**
     * Recupera el perfil asociado a un usuario.
     */
    Optional<UserProfile> findByUserId(Long userId);

    /**
     * Comprueba si existe un perfil asociado a un usuario.
     */
    boolean existsByUserId(Long userId);
}
