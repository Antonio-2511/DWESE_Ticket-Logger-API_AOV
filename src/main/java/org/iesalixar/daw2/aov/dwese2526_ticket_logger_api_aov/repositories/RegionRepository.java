package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories;

import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * DAO de regiones.
 * Define lo básico para consultar y modificar la tabla de regiones
 * siguiendo la convención de Spring Data JPA.
 */
public interface RegionRepository extends JpaRepository<Region, Long> {

    /**
     * Comprueba si existe una región con el código indicado.
     * Equivalente a existsRegionByCode(code).
     */
    boolean existsByCode(String code);

    /**
     * Comprueba si existe una región con el código indicado excluyendo un id.
     * Equivalente a existsRegionByCodeAndNotId(code, id).
     */
    boolean existsByCodeAndIdNot(String code, Long id);

    /**
     * Busca una región por id cargando también sus provincias.
     */
    @Query("select r from Region r left join fetch r.provinces where r.id = :id")
    Optional<Region> findByIdWithProvinces(@Param("id") Long id);
}
