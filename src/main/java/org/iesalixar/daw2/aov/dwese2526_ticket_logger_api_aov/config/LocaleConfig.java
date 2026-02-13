package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Clase de configuración para la gestión de la internacionalización (i18n) en la aplicación.
 *
 * Esta clase define cómo se gestionan los idiomas en las vistas,
 * permitiendo cambiar de idioma mediante un parámetro en la URL (por ejemplo: ?lang=en).
 */
@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    // Logger para registrar información sobre la configuración
    private static final Logger logger = LoggerFactory.getLogger(LocaleConfig.class);

    /**
     * Define el bean responsable de determinar la configuración regional (locale) actual.
     *
     * En este caso, se usa un SessionLocaleResolver, que guarda la configuración de idioma
     * en la sesión del usuario.
     *
     * @return LocaleResolver configurado con el idioma por defecto (español).
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        // Establece el idioma por defecto a español (es)
        slr.setDefaultLocale(Locale.of("es"));
        logger.info(">>> LocaleResolver configurado con idioma por defecto: es");
        return slr;
    }

    /**
     * Define un interceptor que permite cambiar el idioma mediante un parámetro de la URL.
     *
     * Ejemplo: /users?lang=en cambiará el idioma de la interfaz a inglés.
     *
     * @return LocaleChangeInterceptor configurado con el parámetro 'lang'.
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        // Nombre del parámetro que se usará en la URL para cambiar el idioma
        interceptor.setParamName("lang");
        logger.info(">>> LocaleChangeInterceptor configurado con parámetro: lang");
        return interceptor;
    }

    /**
     * Registra el interceptor en la configuración de Spring MVC.
     *
     * Esto asegura que el cambio de idioma sea procesado correctamente
     * en todas las peticiones HTTP.
     *
     * @param registry Registro de interceptores de la aplicación
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        logger.info(">>> LocaleChangeInterceptor añadido al registro de interceptores");
    }
}
