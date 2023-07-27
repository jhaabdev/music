package com.openwt.music.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super(String.format("User with id %s not found.", id));
    }
    public UserNotFoundException(String username) {
        super(String.format("User with username %s not found.", username));
    }
    public UserNotFoundException(Long id, String role) {
        super(String.format("User with role %s and id %s not found.", role, id));
    }
    public UserNotFoundException(String name, String role) {
        super(String.format("User with role %s and name %s not found", role, name));
    }
}
