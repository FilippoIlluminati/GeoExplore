package Geoexplore.User;

import java.util.Set;

public enum UserRole {
    TURISTA_AUTENTICATO(Set.of("VIEW_CONTENT")),
    CONTRIBUTOR(Set.of("VIEW_CONTENT", "UPLOAD_CONTENT")),
    CONTRIBUTOR_AUTORIZZATO(Set.of("VIEW_CONTENT", "UPLOAD_CONTENT", "SKIP_VALIDATION")),
    ANIMATORE(Set.of("VIEW_CONTENT", "UPLOAD_CONTENT", "VALIDATE_CONTENT", "CREATE_CONTEST")),
    CURATORE(Set.of("VIEW_CONTENT", "UPLOAD_CONTENT", "VALIDATE_CONTENT", "MANAGE_CONTENT")),
    GESTORE_PIATTAFORMA(Set.of("VIEW_CONTENT", "UPLOAD_CONTENT", "VALIDATE_CONTENT", "MANAGE_CONTENT", "MANAGE_USERS"));

    private final Set<String> permissions;

    UserRole(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getPermissions() {
        return permissions;
    }
}
