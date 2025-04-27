package com.technokratos.vcs2.exception.registration;

import com.technokratos.vcs2.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class RegistrationException extends ServiceException {
    public RegistrationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
