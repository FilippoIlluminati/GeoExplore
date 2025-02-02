package Geoexplore.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disabilita CSRF per test e sviluppo
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/login", "/register").permitAll() // Accesso pubblico per autenticazione e registrazione
                        .requestMatchers("/admin/**").hasRole("GESTORE_PIATTAFORMA") // Solo GESTORE_PIATTAFORMA può accedere agli endpoint /admin
                        .requestMatchers("/content/upload").hasAuthority("UPLOAD_CONTENT") // Permesso per upload contenuti
                        .requestMatchers("/content/validate").hasAuthority("VALIDATE_CONTENT") // Permesso per validare contenuti
                        .anyRequest().permitAll() // Rende tutte le richieste pubbliche (come richiesto)
                )
                .formLogin(login -> login
                        .loginPage("/login") // URL della pagina di login personalizzata
                        .defaultSuccessUrl("/", true) // Reindirizzamento alla home dopo login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable()) // Permette frame per H2 Console
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Encoder per proteggere le password
    }
}
