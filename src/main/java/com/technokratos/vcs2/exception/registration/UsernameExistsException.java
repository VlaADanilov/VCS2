package com.technokratos.vcs2.exception.registration;

public class UsernameExistsException extends RegistrationException{
    public UsernameExistsException(String username) {
        super("User with username \"%s\" already exists".formatted(username));
    }
}
