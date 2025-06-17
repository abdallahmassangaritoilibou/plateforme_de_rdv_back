package com.mon_rdv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebSecurityConfig {

    // Ce bean configure la chaîne de filtres de sécurité (SecurityFilterChain)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Désactive la protection CSRF (Cross-Site Request Forgery)
                // Utile pour simplifier les appels API, mais à activer si j'utilise des
                // formulaires web
                .csrf(csrf -> csrf.disable())

                // Définition des règles d'accès aux URLs
                .authorizeHttpRequests(auth -> auth
                        // Autorise l'accès sans authentification aux endpoints spécifiés
                        .anyRequest().permitAll() // PERMET TOUT POUR LES TESTS
                );
                   return http.build();            
    }    

    // Bean qui crée un encodeur de mot de passe BCrypt pour sécuriser les mots de
    // passe utilisateurs
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
