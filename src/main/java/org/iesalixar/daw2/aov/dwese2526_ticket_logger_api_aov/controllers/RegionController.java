package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.RegionCreateDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.RegionDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.RegionDetailDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.RegionUpdateDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.DuplicateResourceException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories.RegionRepository;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services.RegionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springdoc.core.annotations.ParameterObject;


import java.net.URI;
import java.util.Locale;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RegionService regionService;

    // =========================
    // LISTAR REGIONES
    // =========================

    @Operation(
            summary = "Listar regiones",
            description = "Devuelve una lista paginada de regiones."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado paginado de regiones",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })

    @GetMapping
    public ResponseEntity<Page<RegionDTO>> listRegions(
            @ParameterObject
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {

        logger.info("Listando regiones page={}, size={}, sort={}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort());

        Page<RegionDTO> page = regionService.list(pageable);

        return ResponseEntity.ok(page);
    }


    // =========================
    // CREAR REGIÓN
    // =========================

    @Operation(
            summary = "Crear región",
            description = "Permite registrar una nueva región en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Región creada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Código de región duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<RegionDTO> createRegion(@Valid @RequestBody RegionCreateDTO dto) {

        RegionDTO created = regionService.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    // =========================
    // ACTUALIZAR REGIÓN
    // =========================

    @Operation(
            summary = "Actualizar región",
            description = "Permite actualizar los datos de una región existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Región actualizada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Región no encontrada"),
            @ApiResponse(responseCode = "409", description = "Código duplicado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RegionDTO> updateRegion(
            @PathVariable Long id,
            @Valid @RequestBody RegionUpdateDTO dto) {

        logger.info("Actualizando región con ID {}", id);

        dto.setId(id);

        RegionDTO updated = regionService.update(dto);

        return ResponseEntity.ok(updated);
    }

    // =========================
    // OBTENER REGIÓN POR ID
    // =========================

    @Operation(
            summary = "Obtener región por ID",
            description = "Devuelve el detalle de una región específica."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Región encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegionDetailDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Región no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RegionDetailDTO> getRegionById(@PathVariable Long id) {

        logger.info("Mostrando detalle de la región con ID {}", id);

        RegionDetailDTO regionDTO = regionService.getDetail(id);

        return ResponseEntity.ok(regionDTO);
    }

    // =========================
    // ELIMINAR REGIÓN
    // =========================

    @Operation(
            summary = "Eliminar región",
            description = "Permite eliminar una región del sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Región eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Región no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {

        logger.info("Eliminando región con ID {}", id);

        regionService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
