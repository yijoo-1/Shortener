package com.dev.shortener.domain.url.service;


import com.dev.shortener.domain.url.entity.Url;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UrlDeleter {

    private final UrlReader urlReader;

    public UrlDeleter(UrlReader urlReader) {
        this.urlReader = urlReader;
    }

    public void delete(Long urlId) {
        Url findUrl = urlReader.findById(urlId);
        findUrl.delete();
    }
}

