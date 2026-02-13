package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionDTO {
    private long id;
    private String code;
    private String name;
}
