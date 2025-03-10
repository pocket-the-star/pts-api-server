package com.pts.api.lib.internal.shared.enums;

public enum UserRole {
    ADMIN("ADMIN"),
    SELLER("SELLER"),
    NORMAL("NORMAL");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static UserRole of(String role) {
        for (UserRole userRole : values()) {
            if (userRole.getRole().equals(role)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + role);
    }
}
