package com.technokratos.vcs2.exception.notFound;

import java.util.UUID;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String username) {
        super("User with name not found: %s".formatted(username), "/");
    }
    public UserNotFoundException(UUID id) {
        super("User with id not found: %s".formatted(id), "/");
    }
}
