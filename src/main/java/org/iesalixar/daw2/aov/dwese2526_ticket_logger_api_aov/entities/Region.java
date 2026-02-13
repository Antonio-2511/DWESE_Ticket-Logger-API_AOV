package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * La clase `Region` representa una entidad que modela una región dentro de la base de datos.
 * Contiene tres campos: `id`, `code` y `name`.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="regions")
public class Region {


    // Identificador único (autogenerado por la BD)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo que almacena el código de la región, normalmente una cadena corta que identifica la región.
// Ejemplo: "01" para Andalucía.
    @Column(name = "code", nullable = false, length = 2) // Define la columna correspondiente en la tabla.
    private String code;

    // Campo que almacena el nombre completo de la región, como "Andalucía" o "Cataluña".
    @Column(name = "name", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String name;

    /**
     * Lista de provincias pertenecientes a la región.
     * - LAZY: no se cargan hasta que se accede a 'provinces'.
     * - mappedBy: el dueño de la relación es Province.region.
     * - Con cascade ALL: así se borrarían las provincias asociadas a la región si se elimina la región.
     */
    @OneToMany(
            mappedBy = "region",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            orphanRemoval = false
    )
    @ToString.Exclude   // Evita bucles en toString()
    @EqualsAndHashCode.Exclude   // Evita ciclos en equals/hashCode
    private List<Province> provinces = new ArrayList<>();


    // Constructor sin ID (para insertar nuevas regiones)
    public Region(String code, String name) {
        this.code = code;
        this.name = name;
    }
}





