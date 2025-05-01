package com.technokratos.vcs2.exception;

import org.springframework.http.HttpStatus;

public class ImageExistInThisEmployeeException extends ServiceException {
    public ImageExistInThisEmployeeException() {
        super("Image in this employee already exist`s", HttpStatus.BAD_REQUEST);
    }
}
