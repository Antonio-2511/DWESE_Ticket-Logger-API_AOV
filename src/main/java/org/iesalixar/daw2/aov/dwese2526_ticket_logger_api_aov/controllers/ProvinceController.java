package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.controllers;

import jakarta.validation.Valid;
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
@Controller
@RequestMapping("/provinces")
public class ProvinceController {

    private static final Logger logger =
            LoggerFactory.getLogger(ProvinceController.class);

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private MessageSource messageSource;

    // -------------------------------------------------
    // LISTADO
    // -------------------------------------------------

    @GetMapping
    public String listProvinces(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable,
            Model model,
            Locale locale) {

        logger.info("Listando provincias page={}, size={}, sort={}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort());

        try {

            Page<ProvinceDTO> page = provinceService.list(pageable);
            model.addAttribute("page", page);

            String sortParam = "name,asc";
            if (page.getSort().isSorted()) {
                Sort.Order order = page.getSort().iterator().next();
                sortParam = order.getProperty() + "," +
                        order.getDirection().name().toLowerCase();
            }
            model.addAttribute("sortParam", sortParam);

        } catch (Exception e) {

            logger.error("Error al listar las provincias: {}", e.getMessage(), e);

            String errorMessage = messageSource.getMessage(
                    "msg.province-controller.list.error",
                    null,
                    locale
            );

            model.addAttribute("errorMessage", errorMessage);
        }

        return "views/province/province-list";
    }



    // -------------------------------------------------
    // CREAR
    // -------------------------------------------------

    @GetMapping("/new")
    public String showNewForm(Model model, Locale locale) {

        logger.info("Mostrando formulario para nueva provincia");

        model.addAttribute("province", new ProvinceCreateDTO());
        model.addAttribute("regions",
                provinceService.listRegionsForSelect());


        return "views/province/province-form";
    }

    @PostMapping("/insert")
    public String insertProvince(
            @Valid @ModelAttribute("province") ProvinceCreateDTO provinceDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model,
            Locale locale) {

        logger.info("Insertando nueva provincia con código {}",
                provinceDTO.getCode());

        if (result.hasErrors()) {

            model.addAttribute(
                    "listRegions",
                    provinceService.listRegionsForSelect()
            );

            return "views/province/province-form";
        }

        try {

            provinceService.create(provinceDTO);

            logger.info("Provincia {} insertada con éxito.",
                    provinceDTO.getCode());

            return "redirect:/provinces";

        } catch (DuplicateResourceException ex) {

            logger.warn("El código de la provincia {} ya existe.",
                    provinceDTO.getCode());

            String errorMessage = messageSource.getMessage(
                    "msg.province-controller.insert.codeExist",
                    null,
                    locale
            );
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/provinces/new";

        } catch (Exception e) {

            logger.error("Error al insertar la provincia {}: {}",
                    provinceDTO.getCode(), e.getMessage(), e);

            String errorMessage = messageSource.getMessage(
                    "msg.province-controller.insert.error",
                    null,
                    locale
            );
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/provinces/new";
        }
    }


    // -------------------------------------------------
    // EDITAR
    // -------------------------------------------------

    @GetMapping("/edit")
    public String showEditForm(
            @RequestParam("id") Long id,
            Model model,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Mostrando formulario de edición para provincia con ID {}", id);

        try {

            ProvinceUpdateDTO provinceDTO = provinceService.getForEdit(id);
            model.addAttribute("province", provinceDTO);

            model.addAttribute(
                    "listRegions",
                    provinceService.listRegionsForSelect()
            );

            return "views/province/province-form";

        } catch (ResourceNotFoundException ex) {

            String msg = messageSource.getMessage(
                    "msg.province-controller.edit.notfound",
                    null,
                    locale
            );

            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/provinces";
        }
    }


    @PostMapping("/update")
    public String updateProvince(
            @Valid @ModelAttribute("province") ProvinceUpdateDTO dto,
            BindingResult br,
            RedirectAttributes redirectAttributes,
            Model model,
            Locale locale) {

        if (br.hasErrors()) {

            model.addAttribute(
                    "listRegions",
                    provinceService.listRegionsForSelect()
            );

            return "views/province/province-form";
        }

        logger.info("Actualizando provincia con ID {}", dto.getId());

        try {

            provinceService.update(dto);
            return "redirect:/provinces";

        } catch (DuplicateResourceException ex) {

            logger.warn(
                    "Código {} ya existe para otra provincia",
                    dto.getCode()
            );

            String msg = messageSource.getMessage(
                    "msg.province-controller.update.codeExist",
                    null,
                    locale
            );

            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/provinces/edit?id=" + dto.getId();

        } catch (ResourceNotFoundException ex) {

            String msg = messageSource.getMessage(
                    "msg.province-controller.detail.notFound",
                    null,
                    locale
            );

            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/provinces";
        }
    }




    // -------------------------------------------------
    // BORRAR
    // -------------------------------------------------

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Eliminando provincia con ID {}", id);

        try {

            provinceService.delete(id);

            String successMessage = messageSource.getMessage(
                    "msg.province-controller.delete.ok",
                    null,
                    locale
            );

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    successMessage
            );

            return "redirect:/provinces";

        } catch (Exception e) {

            logger.error("Error al eliminar la provincia con ID {}: {}",
                    id, e.getMessage(), e);

            String errorMessage = messageSource.getMessage(
                    "msg.province-controller.delete.error",
                    null,
                    locale
            );

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    errorMessage
            );

            return "redirect:/provinces";
        }
    }


    // -------------------------------------------------
    // DETALLE
    // -------------------------------------------------

    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             Locale locale) {

        logger.info("Mostrando detalle de la provincia con ID {}", id);

        try {

            ProvinceDetailDTO provinceDTO =
                    provinceService.getDetail(id);

            model.addAttribute("province", provinceDTO);
            return "views/province/province-detail";

        } catch (ResourceNotFoundException ex) {

            String msg = messageSource.getMessage(
                    "msg.province-controller.detail.notFound",
                    null,
                    locale
            );
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/provinces";

        } catch (Exception e) {

            logger.error("Error al obtener el detalle de la provincia {}: {}",
                    id, e.getMessage(), e);

            String msg = messageSource.getMessage(
                    "msg.province-controller.detail.error",
                    null,
                    locale
            );
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/provinces";
        }
    }


    @PostMapping
    public String insert(
            @Valid ProvinceCreateDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        if (bindingResult.hasErrors()) {
            return "views/province/province-form";
        }

        provinceService.create(dto);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                messageSource.getMessage(
                        "province.create.success",
                        null,
                        locale
                )
        );

        return "redirect:/province";
    }


    @PostMapping("/delete")
    public String deleteProvince(
            @RequestParam("id") Long id,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        logger.info("Eliminando provincia con ID {}", id);

        try {

            provinceService.delete(id);

            return "redirect:/provinces";

        } catch (ResourceNotFoundException ex) {

            String notFoundMessage = messageSource.getMessage(
                    "msg.province-controller.detail.notFound",
                    null,
                    locale
            );

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    notFoundMessage
            );

            return "redirect:/provinces";
        }
    }



}
