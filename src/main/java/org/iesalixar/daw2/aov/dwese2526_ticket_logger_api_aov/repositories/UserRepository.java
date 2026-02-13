package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories;

import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por email.
     * Se cargan también los roles para evitar problemas Lazy en Spring Security.
     */
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    /**
     * Comprueba si existe un usuario con el email indicado.
     * Usado en OAuth2 (CustomOAuth2SuccessHandler).
     */
    boolean existsByEmail(String email);

    /**
     * Comprueba si existe otro usuario con el mismo email excluyendo un id.
     * Usado en edición de usuarios.
     */
    boolean existsByEmailAndIdNot(String email, Long id);


    /**
     * Localiza un usuario por email (ignorando mayúsculas/minúsculas) y asegura que sus roles
     * queden cargados en la misma consulta.
     *
     * @param email email del usuario (usado como identificador/username del sistema).
     * @return {@link java.util.Optional} con el usuario y sus roles; {@code Optional.empty()} si no existe.
     */
    Optional<User> findByEmailIgnoreCase(String email);

}
