package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.dtos.*;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.entities.User;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.DuplicateResourceException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.exceptions.ResourceNotFoundException;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.mappers.UserMapper;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories.RoleRepository;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.repositories.UserRepository;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MessageSource messageSource;



    @GetMapping
    public String listUsers(
            @PageableDefault(size = 10, sort = "email", direction = Sort.Direction.ASC)
            Pageable pageable,
            Model model) {

        Page<UserDTO> page = userService.list(pageable);

        model.addAttribute("page", page);

        String sortParam = "email,asc";
        if (page.getSort().isSorted()) {
            Sort.Order order = page.getSort().iterator().next();
            sortParam = order.getProperty() + "," +
                    order.getDirection().name().toLowerCase();
        }
        model.addAttribute("sortParam", sortParam);

        return "views/users/user-list";
    }



    @GetMapping("/new")
    public String showNewForm(Model model) {
        model.addAttribute("user", new UserCreateDTO());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "views/users/user-form";
    }

    @PostMapping("/insert")
    public String insertUser(
            @Valid @ModelAttribute("user") UserCreateDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model,
            Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            return "views/users/user-form";
        }

        try {
            userService.create(dto);
            return "redirect:/users";

        } catch (DuplicateResourceException ex) {

            String msg = messageSource.getMessage(
                    "msg.user-controller.insert.emailExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/users/new";
        }
    }



    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id,
                               Model model,
                               RedirectAttributes redirectAttributes,
                               Locale locale) {

        try {

            model.addAttribute("user", userService.getForEdit(id));
            model.addAttribute("allRoles", roleRepository.findAll());
            return "views/users/user-form";

        } catch (ResourceNotFoundException ex) {

            String msg = messageSource.getMessage(
                    "msg.user-controller.edit.notfound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/users";
        }
    }

    @PostMapping("/update")
    public String updateUser(
            @Valid @ModelAttribute("user") UserUpdateDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model,
            Locale locale) {

        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            return "views/users/user-form";
        }

        try {

            userService.update(dto);
            return "redirect:/users";

        } catch (DuplicateResourceException ex) {

            String msg = messageSource.getMessage(
                    "msg.user-controller.update.usernameExist", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/users/edit?id=" + dto.getId();

        } catch (ResourceNotFoundException ex) {

            String msg = messageSource.getMessage(
                    "msg.user-controller.edit.notfound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/users";
        }
    }



    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id,
                             RedirectAttributes redirectAttributes,
                             Locale locale) {

        try {
            userService.delete(id);
            return "redirect:/users";

        } catch (ResourceNotFoundException ex) {

            String msg = messageSource.getMessage(
                    "msg.user-controller.detail.notFound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/users";
        }
    }


    @GetMapping("/detail")
    public String showDetail(@RequestParam("id") Long id,
                             Model model,
                             RedirectAttributes redirectAttributes,
                             Locale locale) {

        try {

            model.addAttribute("user", userService.getDetail(id));
            return "views/users/user-detail";

        } catch (ResourceNotFoundException ex) {

            String msg = messageSource.getMessage(
                    "msg.user-controller.detail.notFound", null, locale);
            redirectAttributes.addFlashAttribute("errorMessage", msg);
            return "redirect:/users";
        }
    }
}
