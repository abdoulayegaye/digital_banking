package com.banking.digitalbankingproject.enums;

public enum Role {
    ADMIN,
    EMPLOYEE,
    USER;
    
    public static Role fromString(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return USER; // valeur par défaut
        }
    }
} 