package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services;

import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserDTO> list(Pageable pageable);

    UserUpdateDTO getForEdit(Long id);

    void create(UserCreateDTO dto);

    void update(UserUpdateDTO dto);

    void delete(Long id);

    UserDetailDTO getDetail(Long id);
}
