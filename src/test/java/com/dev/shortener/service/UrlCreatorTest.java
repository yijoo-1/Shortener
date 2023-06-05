
package com.dev.shortener.service;


import com.dev.shortener.domain.url.dto.UrlCreateRequest;
import com.dev.shortener.domain.url.dto.UrlCreateResponse;
import com.dev.shortener.domain.url.entity.ShortUrlStatus;
import com.dev.shortener.domain.url.entity.Url;
import com.dev.shortener.domain.url.repository.UrlRepository;
import com.dev.shortener.domain.url.service.UrlCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest
class UrlCreatorTest {

	@Autowired
	private UrlRepository urlRepository;

	@Autowired
	private UrlCreator urlCreator;

	@AfterEach
	void tearDown() {
		urlRepository.deleteAll();
	}

	@Test
	@DisplayName("ShortUrl을 생성하고 나면 상태는 ACTIVE이다.")
	void create() {
		// given
		UrlCreateRequest urlCreateRequest =
			new UrlCreateRequest("https://github.com/yijoo-1", false);

		UrlCreateResponse urlCreateResponse = urlCreator.create("https://github.com/", urlCreateRequest);

		// when
		Url findUrl = urlRepository.findById(urlCreateResponse.id()).get();

		// then
		assertThat(findUrl.status()).isSameAs(ShortUrlStatus.ACTIVE);
	}
}