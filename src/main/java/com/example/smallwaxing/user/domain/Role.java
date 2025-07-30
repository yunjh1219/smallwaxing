package com.example.smallwaxing.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");
    private final String key;

    public static Role ofValue(String value) {
        Role result;

        if (Objects.equals(value, "ROLE_ADMIN")) {
            result = Role.ADMIN;
        } else if (Objects.equals(value, "ROLE_USER")) {
            result = Role.USER;
        } else {
            result = null;
        }

        return result;
    }
}
