package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.UserProfileDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.UserProfilePatchDTO;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.InvalidFileException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories.UserProfileRepository;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories.UserRepository;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services.FileStorageService;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Locale;

@RestController
@RequestMapping("/api/profile")
@Validated
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserProfileService userProfileService;



    /**
     * GET API: devuelve el perfil para el usuario autenticado.
     * Equivale a "mostrar formulario", pero en API devuelves datos.
     */
    @GetMapping
    public ResponseEntity<UserProfileDTO> getMyProfile(Principal principal) {

        String email = principal.getName();
        logger.info("API getMyProfile para {}", email);

        UserProfileDTO dto = userProfileService.getFormByEmail(email);

        return ResponseEntity.ok(dto);
    }


    /**
     * Actualiza parcialmente el perfil del usuario autenticado (PATCH).
     *
     * <p>Consume <b>multipart/form-data</b> con:</p>
     * <ul>
     *   <li><b>profile</b>: JSON con los campos a modificar (solo se actualizan los presentes).</li>
     *   <li><b>profileImageFile</b> (opcional): nueva imagen de perfil.</li>
     * </ul>
     *
     * <p>El usuario se identifica a partir del {@link Principal}.</p>
     *
     * @param patchDto datos parciales del perfil a aplicar
     * @param profileImageFile imagen de perfil opcional
     * @param principal usuario autenticado
     * @return perfil actualizado
     */
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserProfileDTO> patchMyProfile(

            @ModelAttribute UserProfilePatchDTO patchDto,
            @RequestPart(value = "profileImageFile", required = false) MultipartFile profileImageFile,
            Principal principal
    ) {

        String email = principal.getName();
        logger.info("API patchMyProfile para {}", email);

        userProfileService.updateProfile(email, patchDto, profileImageFile);

        UserProfileDTO updated = userProfileService.getFormByEmail(email);

        return ResponseEntity.ok(updated);
    }


}
