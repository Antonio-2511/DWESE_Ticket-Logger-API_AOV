package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase {@code Province} representa una provincia dentro del sistema.
 *
 * Cada provincia tiene:
 *
 * <ul>
 *     <li>{@code id}: identificador único (clave primaria, autogenerado en BD).</li>
 *     <li>{@code code}: código único de la provincia (ej. "SE", "MA", "GR").</li>
 *     <li>{@code name}: nombre de la provincia (ej. "Sevilla").</li>
 *     <li>{@code region}: objeto {@link Region} al que pertenece la provincia
 *         (equivale a la FK {@code region_id} en la base de datos).</li>
 * </ul>
 *
 * Se sigue el mismo estilo que {@link Region}, usando Lombok y Bean Validation.
 * A nivel de BD seguirá existiendo la columna {@code region_id}, pero en la capa
 * de dominio trabajamos con el objeto {@code Region}.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="provinces")
public class Province {

    // Identificador único de la provincia (AUTO_INCREMENT en la tabla `provinces`).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código único de la provincia (VARCHAR(10) NOT NULL UNIQUE).
    @Column(name = "code", nullable = false, length = 2) // Define la columna correspondiente en la tabla.
    private String code;

    // Nombre de la provincia (VARCHAR(100) NOT NULL).
    @Column(name = "name", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String name;

    // Relación con la entidad `Region`, representando la comunidad autónoma a la que pertenece la provincia.
    @ManyToOne(fetch = FetchType.LAZY) // Relación de muchas provincias a una región.
    @JoinColumn(name = "region_id", nullable = false) // Clave foránea en la tabla provinces que referencia a la tabla regions.
    private Region region;

    /**
     * Validación derivada:
     * Debe haberse seleccionado una región válida (region != null y region.id != null).
     * Esto evita el problema de tener un objeto Region vacío que cumple @NotNull.
     */
    @AssertTrue(message = "{msg.province.region.notNull}")
    public boolean isRegionSelected() {
        return region != null && region.getId() != null;
    }

    /**
     * Constructor de conveniencia sin {@code id},
     * útil para altas donde el ID se autogenera en base de datos.
     *
     * @param code   código de la provincia.
     * @param name   nombre de la provincia.
     * @param region región asociada (debe tener al menos el id informado).
     */
    public Province(String code, String name, Region region) {
        this.code = code;
        this.name = name;
        this.region = region;
    }



}

