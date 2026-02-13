package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services;

import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.UserProfileDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.UserProfilePatchDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileService {

    UserProfileDTO getFormByEmail(String email);

    void updateProfile(String email, UserProfilePatchDTO patchDto, MultipartFile profileImageFile);
}

