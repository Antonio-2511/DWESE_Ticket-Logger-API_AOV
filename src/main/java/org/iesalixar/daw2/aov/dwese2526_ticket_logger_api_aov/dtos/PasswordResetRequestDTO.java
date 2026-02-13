package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetRequestDTO {
    @NotBlank
    @Email
    private String email;
}

