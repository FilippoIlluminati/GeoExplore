package Geoexplore.User;

import java.util.Set;

public enum UserRole {
    TURISTA(Set.of("VIEW_CONTENT")),
    TURISTA_AUTENTICATO(Set.of("VIEW_CONTENT", "UPLOAD_PHOTOS", "REPORT_CONTENT")),
    CONTRIBUTOR(Set.of("UPLOAD_CONTENT", "PARTICIPATE_CONTEST")),
    CONTRIBUTOR_AUTORIZZATO(Set.of("UPLOAD_CONTENT", "BYPASS_APPROVAL")),
    ANIMATORE(Set.of("CREATE_CONTEST", "VALIDATE_CONTENT")),
    CURATORE(Set.of("VALIDATE_CONTENT", "POST_SOCIAL")),
    GESTORE_PIATTAFORMA(Set.of("MANAGE_USERS", "CREATE_USERS", "APPROVE_CONTRIBUTORS", "MANAGE_SYSTEM"));

    private final Set<String> permissions;

    UserRole(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public boolean hasPermission(String permission) {
        return permissions.contains(permission);
    }
}
