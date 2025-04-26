package com.technokratos.vcs2.exception;

public class EmailExistsEsception extends RegistrationException{
    public EmailExistsEsception(String email) {
        super("User with email \"%s\" already exists".formatted(email));
    }
}
