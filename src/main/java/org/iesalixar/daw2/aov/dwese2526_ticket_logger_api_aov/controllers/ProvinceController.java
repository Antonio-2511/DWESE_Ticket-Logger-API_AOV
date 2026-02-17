package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.*;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.DuplicateResourceException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services.ProvinceService;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services.RegionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

/**
 * Controlador encargado de gestionar las operaciones CRUD de las Provincias.
 *
 * Maneja las rutas bajo "/provinces".
 */
@RestController
@RequestMapping("/api/provinces")
@RequiredArgsConstructor
public class ProvinceController {

    private final ProvinceService provinceService;

    // LISTAR
    @GetMapping
    public ResponseEntity<Page<ProvinceDTO>> list(
            @PageableDefault(size = 10, sort = "name")
            Pageable pageable) {

        return ResponseEntity.ok(provinceService.list(pageable));
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<ProvinceDTO> getById(@PathVariable Long id) {

        return ResponseEntity.ok(provinceService.getById(id));
    }

    // CREAR
    @PostMapping
    public ResponseEntity<ProvinceDTO> create(
            @Valid @RequestBody ProvinceCreateDTO dto) {

        ProvinceDTO created = provinceService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<ProvinceDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProvinceUpdateDTO dto) {

        dto.setId(id);
        return ResponseEntity.ok(provinceService.update(dto));
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        provinceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
