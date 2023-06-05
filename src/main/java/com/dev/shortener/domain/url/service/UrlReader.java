package com.dev.shortener.domain.url.service;

import com.dev.shortener.domain.url.entity.Url;
import com.dev.shortener.domain.url.exception.UrlInvalidStatusException;
import com.dev.shortener.domain.url.repository.UrlRepository;
import com.dev.shortener.domain.url.exception.UrlNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UrlReader {

    private final UrlRepository urlRepository;

    public UrlReader(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Url getUrlIfValid(String shortenedUrl) {
        Url findUrl = findByShortenedUrl(shortenedUrl);

        if (!findUrl.hasValidStatus()) {
            throw new UrlInvalidStatusException(
                    String.format("Url '%d' has invalid status=%s", findUrl.id(), findUrl.status()));
        }
        return findUrl;
    }

    public Url findById(Long urlId) {
        return urlRepository.findById(urlId)
                .orElseThrow(() -> new UrlNotFoundException(
                        String.format("Cannot find Url for id=%d",urlId)));
    }

    public Url findByShortenedUrl(String shortenedUrl) {
        return urlRepository.findUrlByShortenedUrl(shortenedUrl)
                .orElseThrow(() -> new UrlNotFoundException(
                        String.format("Cannot find Url for shortenedUrl=%s",shortenedUrl)));
    }

    public Url findByOriginUrl(String originUrl) {
        return urlRepository.findUrlByOriginUrl(originUrl)
                .orElseThrow(() -> new UrlNotFoundException(
                       String.format("Cannot find Url for originUrl=%s",originUrl)));
    }

    public boolean existsByOriginUrl(String originUrl) {
        return urlRepository.existsByOriginUrl(originUrl);
    }
}
