package com.nizsimsek.blogApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GeneralNotFoundException extends RuntimeException {

    public GeneralNotFoundException(String message) {
        super(message);
    }
}
