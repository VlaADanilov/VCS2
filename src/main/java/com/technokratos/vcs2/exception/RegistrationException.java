package com.technokratos.vcs2.exception;

import org.springframework.http.HttpStatus;

public class RegistrationException extends ServiceException{
    public RegistrationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
