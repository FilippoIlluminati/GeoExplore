package Geoexplore.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disabilita protezione CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permetti accesso a TUTTE le richieste
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())); // Abilita frame per H2

        return http.build();
    }
}
