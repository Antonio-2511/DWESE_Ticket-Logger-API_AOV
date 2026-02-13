package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services;

import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.RegionCreateDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.RegionDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.RegionDetailDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.RegionUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RegionService {

    Page<RegionDTO> list(Pageable pageable);

    RegionUpdateDTO getForEdit(Long id);

    RegionDTO create(RegionCreateDTO dto);

    RegionDTO update(RegionUpdateDTO dto);

    void delete(Long id);

    RegionDetailDTO getDetail(Long id);
}

