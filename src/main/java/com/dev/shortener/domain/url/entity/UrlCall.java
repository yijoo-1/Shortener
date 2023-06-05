package com.dev.shortener.domain.url.entity;


import com.dev.shortener.domain.url.type.BaseTime;
import com.dev.shortener.domain.url.util.TimeUtil;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;


@Entity
public class UrlCall extends BaseTime {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "url_id")
    private Url url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private Referer referer;

    @Column(nullable = false, updatable = false)
    private LocalDateTime callTime;

    protected UrlCall() {
    }

    public UrlCall(Url url, Referer referer, LocalDateTime callTime) {
        this.url = url;
        this.referer = referer;
        this.callTime = callTime;
    }

    public boolean isCallTimeWithin(LocalDateTime startDate, LocalDateTime endDate) {
        return TimeUtil.isDateWithin(this.callTime, startDate, endDate);
    }

    public Long id() {
        return id;
    }

    public Url url() {
        return url;
    }

    public Referer referer() {
        return referer;
    }

    public LocalDateTime callTime() {
        return callTime;
    }
}
