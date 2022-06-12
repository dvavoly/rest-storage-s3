package org.example.storage.model.enums;

public enum Permission {
    USERS_READ("users:read"),
    USERS_WRITE("users:write"),
    FILES_READ("files:read"),
    FILES_WRITE("files:write"),
    EVENTS_DELETE("events:delete");
    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
