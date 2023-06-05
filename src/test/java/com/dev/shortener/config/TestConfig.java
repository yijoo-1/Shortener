package com.dev.shortener.config;


import com.dev.shortener.domain.url.repository.UrlCallRepository;
import com.dev.shortener.domain.url.repository.UrlRepository;
import com.dev.shortener.domain.url.service.UrlCreator;
import com.dev.shortener.domain.url.service.UrlReader;
import com.dev.shortener.domain.url.service.UrlStatisticsProvider;
import com.dev.shortener.domain.url.service.UrlUpdater;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

	@Bean
	public UrlCreator urlCreator(
		UrlRepository urlRepository,
		UrlReader urlReader,
		UrlUpdater urlUpdater
	) {
		return new UrlCreator(urlRepository, urlReader, urlUpdater);
	}

	@Bean
	public UrlReader urlReader(
		UrlRepository urlRepository
	) {
		return new UrlReader(urlRepository);
	}

	@Bean
	public UrlUpdater urlUpdater(
		UrlReader urlReader
	) {
		return new UrlUpdater(urlReader);
	}

	@Bean
	public UrlStatisticsProvider urlStatisticsProvider(
		UrlCallRepository urlCallRepository,
		UrlReader urlReader
	) {
		return new UrlStatisticsProvider(urlCallRepository, urlReader);
	}
}
