package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceDetailDTO {
    private Long id;
    private String code;
    private String name;

    /** Región asociada a la provincia (objeto embebido para el detalle). */
    private RegionDTO region;
}

