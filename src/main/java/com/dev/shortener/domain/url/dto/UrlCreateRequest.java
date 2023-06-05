package com.dev.shortener.domain.url.dto;


public record UrlCreateRequest (
   String url,
   boolean expiration
        ){
    public boolean hasExpirationOption() {
        return this.expiration;
    }
}