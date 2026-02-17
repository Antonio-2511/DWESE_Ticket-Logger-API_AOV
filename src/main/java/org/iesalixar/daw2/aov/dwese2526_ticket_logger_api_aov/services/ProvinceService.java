package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services;

import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProvinceService {

    Page<ProvinceDTO> list(Pageable pageable);

    ProvinceDTO getById(Long id);

    ProvinceDTO create(ProvinceCreateDTO dto);

    ProvinceDTO update(ProvinceUpdateDTO dto);

    void delete(Long id);
}
