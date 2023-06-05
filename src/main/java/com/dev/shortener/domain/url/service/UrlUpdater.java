package com.dev.shortener.domain.url.service;

import com.dev.shortener.domain.url.entity.Url;
import com.dev.shortener.domain.url.util.TimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UrlUpdater {

    private final UrlReader urlReader;

    public UrlUpdater(UrlReader urlReader) {
        this.urlReader = urlReader;
    }

    public void updateExpirationDate(Long urlId, boolean hasExpirationOption) {
        Url findUrl = urlReader.findById(urlId);

        if (hasExpirationOption) {
            findUrl.updateExpirationDate(TimeUtil.getCurrentSeoulTime(), Url.MAX_EXPIRATION_PERIOD);
            return;
        }
        findUrl.updateExpirationDate(TimeUtil.getCurrentSeoulTime(), Url.DEFAULT_EXPIRATION_PERIOD);
    }
}