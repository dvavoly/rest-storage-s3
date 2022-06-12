package org.example.storage.model.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(Set.of(
            Permission.USERS_READ,
            Permission.USERS_WRITE,
            Permission.FILES_READ,
            Permission.FILES_WRITE,
            Permission.EVENTS_DELETE)),
    MODERATOR(Set.of(
            Permission.USERS_READ,
            Permission.FILES_READ,
            Permission.FILES_WRITE,
            Permission.EVENTS_DELETE
    )),
    USER(Set.of(
            Permission.FILES_READ,
            Permission.FILES_WRITE));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthority() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
