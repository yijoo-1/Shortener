package com.dev.shortener.web.api.url;

import com.dev.shortener.domain.url.dto.UrlStatisticsResponse;
import com.dev.shortener.domain.url.service.UrlCaller;
import com.dev.shortener.domain.url.service.UrlStatisticsProvider;
import com.dev.shortener.web.util.RefererParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
public class UrlQueryApi {

	public static final char STATISTICS_QUERY_REQUEST_CHARACTER = '+';

	private final UrlCaller urlCaller;
	private final UrlStatisticsProvider urlStatisticsProvider;

	public UrlQueryApi(UrlCaller urlCaller, UrlStatisticsProvider urlStatisticsProvider) {
		this.urlCaller = urlCaller;
		this.urlStatisticsProvider = urlStatisticsProvider;
	}

	@GetMapping("/{shortenedUrl}")
	public ResponseEntity<?> query(HttpServletRequest request, @PathVariable String shortenedUrl) {
		if (isStatisticsQueryRequest(shortenedUrl)) {
			return getStatistics(shortenedUrl.substring(0, shortenedUrl.length() - 1));
		}
		return redirect(request, shortenedUrl);
	}

	private ResponseEntity<UrlStatisticsResponse> getStatistics(String shortenedUrl) {
		UrlStatisticsResponse response = urlStatisticsProvider.getStatistics(shortenedUrl);
		return ResponseEntity.ok()
			.body(response);
	}

	private ResponseEntity<HttpHeaders> redirect(HttpServletRequest request, String shortenedUrl) {
		String originUrl = urlCaller.getOriginUrlAndRecordCall(RefererParser.parseReferer(request), shortenedUrl);

		HttpHeaders headers = new HttpHeaders();
		URI redirectLocation = UriComponentsBuilder.fromHttpUrl(originUrl)
			.build()
			.toUri();
		headers.setLocation(redirectLocation);

		return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
	}

	private boolean isStatisticsQueryRequest(String shortenedUrl) {
		char lastCharacter = shortenedUrl.charAt(shortenedUrl.length() - 1);
		return lastCharacter == STATISTICS_QUERY_REQUEST_CHARACTER;
	}
}
