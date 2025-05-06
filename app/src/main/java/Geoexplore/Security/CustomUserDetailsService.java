package Geoexplore.Security;

import Geoexplore.User.UserRepository;
import Geoexplore.User.Users;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.stream.Collectors;

/**
 * Implementazione personalizzata di UserDetailsService per il sistema di autenticazione.
 * Carica i dati utente dal database e assegna le autorizzazioni in base al ruolo.
 */
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Costruttore con iniezione del repository
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carica un utente a partire dallo username e restituisce le informazioni
     * necessarie per l'autenticazione (username, password, permessi).
     *
     * @param username lo username fornito in fase di login
     * @return un oggetto User compatibile con Spring Security
     * @throws UsernameNotFoundException se l'utente non è presente nel database
     */
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Utente non trovato: " + username);
        }

        // Converte i permessi del ruolo in authorities Spring Security
        var authorities = user.getRuolo().getPermissions().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
