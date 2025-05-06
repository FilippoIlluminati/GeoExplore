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

    // Configura il servizio personalizzato per il caricamento dei dettagli utente
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    // Configura l'autenticazione usando il provider DAO con encoder
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    // Configura la catena di sicurezza per autorizzare o bloccare le richieste HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, DaoAuthenticationProvider authProvider) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disabilita la protezione CSRF per semplicità (solo in sviluppo)
                .authorizeHttpRequests(auth -> auth
                        // Rotte pubbliche senza autenticazione
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/users/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/poi/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/journeys/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/content/**").permitAll() // Accesso pubblico ai contenuti
                        .requestMatchers(HttpMethod.GET, "/reports/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/reports").permitAll()

                        // Rotte protette da permessi specifici
                        .requestMatchers("/users/create-user").hasAuthority("CREATE_USERS")
                        .requestMatchers("/users/approve-contributor/**").hasAuthority("APPROVE_CONTRIBUTORS")

                        // Qualsiasi altra richiesta richiede autenticazione
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authProvider)
                .httpBasic(); // Autenticazione base HTTP
        return http.build();
    }

    // Bean per la codifica delle password
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
