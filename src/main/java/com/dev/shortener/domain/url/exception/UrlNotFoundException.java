package com.dev.shortener.domain.url.exception;

import com.dev.shortener.global.errors.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UrlNotFoundException extends BusinessException {

    public UrlNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

