package com.technokratos.vcs2.exception.notFound;

import com.technokratos.vcs2.exception.ServiceException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends ServiceException {
    private final String returnToPage;
    public NotFoundException(String message, String returnToPage) {
        super(message, HttpStatus.NOT_FOUND);
        this.returnToPage = returnToPage;
    }
}
