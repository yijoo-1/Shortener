package com.dev.shortener.domain.url.repository;

import com.dev.shortener.domain.url.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UrlRepository extends JpaRepository<Url,Long> {

    Optional<Url> findUrlByShortenedUrl(String shortenedUrl);

    Optional<Url> findUrlByOriginUrl(String originUrl);

    boolean existsByOriginUrl(String originUrl);
}
