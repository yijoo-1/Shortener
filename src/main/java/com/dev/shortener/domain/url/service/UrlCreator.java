package com.dev.shortener.domain.url.service;

import java.time.Period;

import com.dev.shortener.domain.url.dto.UrlCreateRequest;
import com.dev.shortener.domain.url.dto.UrlCreateResponse;
import com.dev.shortener.domain.url.entity.Url;
import com.dev.shortener.domain.url.repository.UrlRepository;
import com.dev.shortener.domain.url.util.Base62Encoder;
import com.dev.shortener.domain.url.util.TimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UrlCreator {

    private final UrlRepository urlRepository;
    private final UrlReader urlReader;
    private final UrlUpdater urlUpdater;

    public UrlCreator(UrlRepository urlRepository, UrlReader urlReader, UrlUpdater urlUpdater) {
        this.urlRepository = urlRepository;
        this.urlReader = urlReader;
        this.urlUpdater = urlUpdater;
    }

    public UrlCreateResponse create(String serverAddress, UrlCreateRequest request) {
        if (isDuplicatedOriginUrl(request.url())) {
            log.info("Create Request for duplicated origin url={}", request.url());
            Url findUrl = urlReader.findByOriginUrl(request.url());
            urlUpdater.updateExpirationDate(findUrl.id(), request.hasExpirationOption());
            return UrlCreateResponse.from(findUrl, serverAddress);
        }
        return createByExpirationOption(serverAddress, request);
    }

    private UrlCreateResponse createByExpirationOption(String serverAddress, UrlCreateRequest request) {
        if (request.hasExpirationOption()) {
            return create(request.url(), serverAddress, Url.DEFAULT_EXPIRATION_PERIOD);
        }
        return create(request.url(), serverAddress, Url.MAX_EXPIRATION_PERIOD);
    }

    private UrlCreateResponse create(String originUrl, String serverAddress, Period expirationPeriod) {
        Url url = saveAsEncoded(originUrl, expirationPeriod);
        return UrlCreateResponse.from(url, serverAddress);
    }

    private Url saveAsEncoded(String originUrl, Period expirationPeriod) {
        Url url = Url.createWithoutShortenedUrl(
                originUrl,
                expirationPeriod,
                TimeUtil.getCurrentSeoulTime()
        );

        urlRepository.save(url);
        url.assignShortenedUrl(Base62Encoder.encode(url.id()));
        return url;
    }

    private boolean isDuplicatedOriginUrl(String originUrl) {
        return urlReader.existsByOriginUrl(originUrl);
    }
}




