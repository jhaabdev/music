package com.openwt.music.enums;

public enum UserRole {
    //USER("ROLE_USER"), anonymous users don't need to register
    ARTIST("ARTIST"),
    ADMIN("ADMIN");

    public final String label;

    private UserRole(String label) {
        this.label = label;
    }
}
