package com.dev.shortener.domain.url.dto;
import com.dev.shortener.domain.url.entity.Url;

public record UrlCreateResponse(
        Long id,
        String shortenedUrl,
        String originUrl
) {

    private static final String ADDRESS_DELIMITER = "/";

    public static UrlCreateResponse from(Url url, String serverAddress) {
        return new UrlCreateResponse(
                url.id(),
                serverAddress + ADDRESS_DELIMITER + url.shortenedUrl(),
                url.originUrl()
        );
    }
}



