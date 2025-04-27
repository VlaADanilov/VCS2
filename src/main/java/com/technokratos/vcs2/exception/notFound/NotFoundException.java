package com.technokratos.vcs2.exception.notFound;

import com.technokratos.vcs2.exception.ServiceException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ServiceException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
