package com.dev.shortener.web.util;

import com.dev.shortener.domain.url.entity.Referer;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class RefererParser {

	private static final String REFERER = "Referer";

	private RefererParser() {
	}

	public static Referer parseReferer(HttpServletRequest request) {
		String referer = request.getHeader(REFERER);
		log.info("referer={}", referer);
		return Referer.parseReferer(referer);
	}
}
