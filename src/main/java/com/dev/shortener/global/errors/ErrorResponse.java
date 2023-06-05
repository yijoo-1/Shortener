package com.dev.shortener.global.errors;

import org.springframework.http.HttpStatus;

public record ErrorResponse (
    HttpStatus status,
    String errorMessage
){
}
