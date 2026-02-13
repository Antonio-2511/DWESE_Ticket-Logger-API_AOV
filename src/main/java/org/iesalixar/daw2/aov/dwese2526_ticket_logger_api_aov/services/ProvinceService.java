package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services;

import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.ProvinceCreateDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.ProvinceDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.ProvinceDetailDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.ProvinceUpdateDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProvinceService {

    Page<ProvinceDTO> list(Pageable pageable);

    ProvinceUpdateDTO getForEdit(Long id);

    void create(ProvinceCreateDTO dto);

    void update(ProvinceUpdateDTO dto);

    void delete(Long id);

    ProvinceDetailDTO getDetail(Long id);

    Object listRegionsForSelect();

}
