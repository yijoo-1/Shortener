package com.dev.shortener.domain.url.service;

import com.dev.shortener.domain.url.entity.Referer;
import com.dev.shortener.domain.url.entity.Url;
import com.dev.shortener.domain.url.entity.UrlCall;
import com.dev.shortener.domain.url.repository.UrlCallRepository;
import com.dev.shortener.domain.url.util.TimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UrlCallCreator {

    private final UrlCallRepository urlCallRepository;

    public UrlCallCreator(UrlCallRepository urlCallRepository) {
        this.urlCallRepository = urlCallRepository;
    }

    public Long create(Url url, Referer referer) {
        UrlCall urlCall = new UrlCall(url, referer, TimeUtil.getCurrentSeoulTime());
        urlCallRepository.save(urlCall);
        return urlCall.id();
    }
}
