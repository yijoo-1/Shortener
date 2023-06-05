package com.dev.shortener.domain.url.service;


import com.dev.shortener.domain.url.dto.UrlStatisticsResponse;
import com.dev.shortener.domain.url.entity.Url;
import com.dev.shortener.domain.url.entity.UrlCall;
import com.dev.shortener.domain.url.repository.UrlCallRepository;
import com.dev.shortener.domain.url.util.TimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UrlStatisticsProvider {

	private static final int STATISTICS_QUERY_DAYS = 7;

	private final UrlCallRepository urlCallRepository;
	private final UrlReader urlReader;

	public UrlStatisticsProvider(UrlCallRepository urlCallRepository, UrlReader urlReader) {
		this.urlCallRepository = urlCallRepository;
		this.urlReader = urlReader;
	}

	public UrlStatisticsResponse getStatistics(String shortenedUrl) {
		Url findUrl = urlReader.findByShortenedUrl(shortenedUrl);
		List<UrlCall> urlCalls = urlCallRepository.findUrlCallByUrl(findUrl);
		List<UrlCall> urlCallsWithinDays = filterWithinDaysFromCurrentDate(urlCalls, STATISTICS_QUERY_DAYS);
		return UrlStatisticsResponse.of(findUrl, urlCallsWithinDays);
	}

	private List<UrlCall> filterWithinDaysFromCurrentDate(List<UrlCall> urlCalls, int days) {
		LocalDateTime currentDateTime = TimeUtil.getCurrentSeoulTime();
		return filterByDuration(urlCalls, currentDateTime.minusDays(days), currentDateTime);
	}

	private List<UrlCall> filterByDuration(List<UrlCall> urlCalls, LocalDateTime startDate, LocalDateTime endDate) {
		return urlCalls.stream()
			.filter(urlCall -> urlCall.isCallTimeWithin(startDate, endDate))
				.collect(Collectors.toList());
	}
}
