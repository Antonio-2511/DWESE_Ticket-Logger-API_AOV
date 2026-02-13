package org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.config;

import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.filters.JwtAuthenticationFilter;
import org.iesalixar.daw2.aov.dwese2526_ticket_logger_api_aov.services.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad de la aplicación.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)  // Activa la seguridad basada en métodos

public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;



    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Bean
    @Order(1)
    public SecurityFilterChain swaggerChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/login", "/logout")
                .csrf(csrf -> csrf.disable()) // swagger/docs no necesitan csrf
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().hasRole("ADMIN")
                )
                // aquí sí activamos login (misma pantalla /login de Spring Security)
                .formLogin(form -> form.permitAll())
                .logout(logout -> logout.logoutUrl("/logout"))
                // importante: swagger con sesión (IF_REQUIRED) para que “recuerde” el login
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }



    /**
     * Cadena de filtros de seguridad HTTP.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 1) API REST: normalmente desactivas CSRF (no hay sesión/cookies)
                .csrf(csrf -> csrf.disable())

                // 2) Sin sesión (stateless) porque autenticamos por token en cada request
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3) No queremos formLogin ni redirecciones
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // 4) Manejo de errores típico API: 401 / 403
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Unauthorized\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Forbidden\"}");
                        })
                )

                // 5) Autorización por rutas (ajusta a /api/**)
                .authorizeHttpRequests(auth -> auth

                        // Endpoints públicos
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/error", "/error/**").permitAll()

                        // Ejemplos por roles (tu lógica, adaptada a /api)
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/regions/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/provinces/**").hasRole("MANAGER")
                        .requestMatchers("/api/profile/**").hasRole("USER")

                        // Todo lo demás requiere token válido
                        .anyRequest().authenticated()
                )

                // 6) Añadir tu filtro JWT antes del filtro de auth de usuario/contraseña
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }




    /**
     * Configura el codificador de contraseñas para cifrar las contraseñas de los usuarios
     * utilizando BCrypt.
     *
     * @return una instancia de {@link PasswordEncoder} que utiliza BCrypt para cifrar contraseñas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Entrando en el método passwordEncoder");
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        logger.info("Saliendo del método passwordEncoder");
        return encoder;
    }

    /**
     * Configura y expone un bean de tipo {@link AuthenticationManager}.
     *
     * En Spring Security, el `AuthenticationManager` es el componente principal que se encarga
     * de procesar solicitudes de autenticación. Este método obtiene la instancia de
     * `AuthenticationManager` configurada automáticamente por Spring a través de
     * `AuthenticationConfiguration` y la expone como un bean disponible en el contexto
     * de la aplicación.
     *
     * @param configuration Objeto de tipo {@link AuthenticationConfiguration} que contiene
     *                      la configuración de autenticación de Spring Security. Este objeto
     *                      incluye los detalles del flujo de autenticación configurado, como
     *                      el proveedor de autenticación y los detalles del usuario.
     *
     * @return Una instancia de {@link AuthenticationManager} configurada con los detalles
     *         especificados en la aplicación.
     *
     * @throws Exception Si ocurre algún error al obtener el `AuthenticationManager`.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {

        // Obtiene y devuelve el AuthenticationManager desde la configuración proporcionada
        return configuration.getAuthenticationManager();
    }


}
