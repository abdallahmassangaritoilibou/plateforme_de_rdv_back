package com.mon_rdv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebSecurityConfig {

    // Ce bean configure la chaîne de filtres de sécurité (SecurityFilterChain)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Désactive la protection CSRF (Cross-Site Request Forgery)
            // Utile pour simplifier les appels API, mais à activer si tu utilises des formulaires web
            .csrf(csrf -> csrf.disable())
            
            // Définition des règles d'accès aux URLs
            .authorizeHttpRequests(auth -> auth
                // Autorise l'accès sans authentification aux endpoints spécifiés
                .requestMatchers("/api/users","/api/users/hello", "/api/users/test-data").permitAll()
                
                // Pour toutes les autres requêtes, une authentification est requise
                .anyRequest().authenticated()
            )
            
            // Active l'authentification HTTP Basic (login via popup navigateur)
            // Remplace ou complète cette ligne selon la méthode d'authentification que tu utilises (JWT, OAuth, etc.)
            .httpBasic(httpBasic -> {})
            
            // Configuration de la gestion des erreurs d'authentification et d'autorisation
            .exceptionHandling(exceptionHandling -> exceptionHandling
                // Personnalise la réponse en cas d'accès non authentifié (401 Unauthorized)
                .authenticationEntryPoint(customAuthenticationEntryPoint())
                // Personnalise la réponse en cas d'accès refusé (403 Forbidden)
                .accessDeniedHandler(customAccessDeniedHandler())
            );

        // Retourne la configuration finale de la chaîne de filtres
        return http.build();
    }

    // Bean qui définit le comportement lors d'une authentification manquante ou échouée (401)
    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setContentType("application/json");   // Réponse au format JSON
            response.setStatus(401);                        // Code HTTP 401 Unauthorized
            // Message JSON envoyé dans le corps de la réponse
            response.getOutputStream().println("{ \"error\": \"Unauthorized - Veuillez vous authentifier.\" }");
        };
    }

    // Bean qui définit le comportement lorsque l'utilisateur est authentifié mais n'a pas les droits (403)
    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setContentType("application/json");   // Réponse au format JSON
            response.setStatus(403);                        // Code HTTP 403 Forbidden
            // Message JSON envoyé dans le corps de la réponse
            response.getOutputStream().println("{ \"error\": \"Forbidden - Accès refusé.\" }");
        };
    }

    // Bean qui crée un encodeur de mot de passe BCrypt pour sécuriser les mots de passe utilisateurs
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
