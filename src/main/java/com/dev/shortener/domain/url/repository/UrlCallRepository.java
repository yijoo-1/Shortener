package com.dev.shortener.domain.url.repository;

import com.dev.shortener.domain.url.entity.Url;
import com.dev.shortener.domain.url.entity.UrlCall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UrlCallRepository extends JpaRepository<UrlCall, Long> {

    List<UrlCall> findUrlCallByUrl(Url url);
}
