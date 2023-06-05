package com.dev.shortener.domain.url.service;

import com.dev.shortener.domain.url.entity.Referer;
import com.dev.shortener.domain.url.entity.Url;
import org.springframework.stereotype.Service;

@Service
public class UrlCaller {

    private final UrlReader urlReader;
    private final UrlCallCreator urlCallCreator;

    public UrlCaller(UrlReader urlReader, UrlCallCreator urlCallCreator) {
        this.urlReader = urlReader;
        this.urlCallCreator = urlCallCreator;
    }

    public String getOriginUrlAndRecordCall(Referer referer, String shortenedUrl) {
        Url validUrl = urlReader.getUrlIfValid(shortenedUrl);
        urlCallCreator.create(validUrl, referer);
        return validUrl.originUrl();
    }
}