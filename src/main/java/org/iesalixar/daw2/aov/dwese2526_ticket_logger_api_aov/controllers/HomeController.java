package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador principal de la aplicación.
 *
 * Este controlador gestiona las peticiones al directorio raíz ("/")
 * y se encarga de mostrar la página principal (index.html).
 */
@Controller
public class HomeController {

    /**
     * Método que maneja las peticiones GET a la raíz del sitio web.
     *
     * @param model Objeto Model utilizado para pasar atributos a la vista (si fueran necesarios).
     * @return El nombre de la plantilla Thymeleaf que se debe renderizar ("index").
     */
    @GetMapping("/")
    public String home(Model model) {
        // Simplemente devuelve la vista "index.html" ubicada en templates/
        return "index";
    }
}
