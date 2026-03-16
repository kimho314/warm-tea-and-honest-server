package com.luna.warmteaandhonestreviews.auth;

import java.util.Arrays;

public enum RoleEnum {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String role;

    RoleEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public static RoleEnum getRole(String role) {
        return Arrays.stream(RoleEnum.values())
            .filter(r -> r.getRole().equals(role))
            .findFirst()
            .orElse(null);
    }
}
