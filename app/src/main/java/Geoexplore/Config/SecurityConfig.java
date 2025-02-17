package Geoexplore.Config;

import Geoexplore.Security.CustomUserDetailsService;
import Geoexplore.User.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // Definisce il bean per il caricamento degli utenti dal database
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // usa il nostro custom user details service
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, DaoAuthenticationProvider authProvider) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disabilita CSRF per le API REST (attenzione in produzione)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()  // Le rotte /auth/** sono libere (registrazione, login)
                        .requestMatchers("/users/all").permitAll()  // L'endpoint per ottenere tutti gli utenti è pubblico
                        .requestMatchers(HttpMethod.GET, "/poi/**").permitAll() // GET dei POI accessibile a tutti
                        .requestMatchers("/users/create-user").hasAuthority("CREATE_USERS")
                        .requestMatchers("/users/approve-contributor/**").hasAuthority("APPROVE_CONTRIBUTORS")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authProvider)
                .httpBasic();  // Abilita l'autenticazione HTTP Basic per le altre rotte
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
