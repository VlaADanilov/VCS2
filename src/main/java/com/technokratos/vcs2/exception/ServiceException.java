package com.technokratos.vcs2.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ServiceException extends RuntimeException {
    private final HttpStatus Status;

    public ServiceException(final String message, final HttpStatus status) {
        super(message);
        this.Status = status;
    }
}

