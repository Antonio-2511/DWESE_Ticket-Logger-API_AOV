package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories;

import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository extends JpaRepository<Province, Long> {

    /**
     * Comprueba si existe una provincia con el código indicado.
     */
    boolean existsByCode(String code);

    /**
     * Comprueba si existe otra provincia con el mismo código excluyendo un id.
     */
    boolean existsByCodeAndIdNot(String code, Long id);
}
