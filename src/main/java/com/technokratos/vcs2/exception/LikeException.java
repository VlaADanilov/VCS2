package com.technokratos.vcs2.exception;

import org.springframework.http.HttpStatus;

public class LikeException extends ServiceException {
    public LikeException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
