package com.dev.shortener.domain.url.exception;

import com.dev.shortener.global.errors.exception.BusinessException;
import org.springframework.http.HttpStatus;

public class UrlInvalidStatusException extends BusinessException {

    public UrlInvalidStatusException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
