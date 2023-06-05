package com.dev.shortener.url;


import com.dev.shortener.domain.url.entity.Referer;
import com.dev.shortener.domain.url.entity.Url;
import com.dev.shortener.domain.url.entity.UrlCall;
import com.dev.shortener.domain.url.repository.UrlCallRepository;
import com.dev.shortener.domain.url.repository.UrlRepository;
import com.dev.shortener.domain.url.util.TimeUtil;
import com.dev.shortener.restdocs.ApiDocumentUtils;
import com.dev.shortener.web.api.url.UrlQueryApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
class UrlQueryApiTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UrlRepository urlRepository;

	@Autowired
	private UrlCallRepository urlCallRepository;

	@Test
	@DisplayName("단축 URL 통계 조회")
	void url_statistics() throws Exception {

		LocalDateTime currentDate = TimeUtil.getCurrentSeoulTime();
		Url url = Url.createWithoutShortenedUrl(
			"https://github.com/yijoo-1", Url.DEFAULT_EXPIRATION_PERIOD, currentDate.minusDays(7));
		String shortenedUrl = "shortenedUrl";
		url.assignShortenedUrl(shortenedUrl);

		urlRepository.save(url);

		List<UrlCall> urlCalls = List.of(
			new UrlCall(url, Referer.DIRECT, currentDate.minusDays(6)),
			new UrlCall(url, Referer.GOOGLE, currentDate.minusDays(6)),
			new UrlCall(url, Referer.DIRECT, currentDate.minusDays(5)),
			new UrlCall(url, Referer.DIRECT, currentDate.minusDays(4)),
			new UrlCall(url, Referer.GOOGLE, currentDate.minusDays(3))
		);

		urlCallRepository.saveAll(urlCalls);

		// when & then
		ResultActions resultActions = mockMvc.perform(
				get("/{shortenedUrl}" + UrlQueryApi.STATISTICS_QUERY_REQUEST_CHARACTER, shortenedUrl))
			.andExpect(status().isOk());

		// REST Docs
		resultActions
			.andDo(print())
			.andDo(document("short-url-statistics",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse(),
				pathParameters(
					parameterWithName("shortenedUrl").description("단축된 URL")
				),
				responseFields(
					fieldWithPath("shortenedUrl").description("단축된 URL"),
					fieldWithPath("originUrl").description("원본 URL"),
					fieldWithPath("viewCount.totalViewCount").description("전체 조회수"),
					fieldWithPath("viewCount.viewCountPerDate[].date").description("일자별 조회수 - 일자"),
					fieldWithPath("viewCount.viewCountPerDate[].viewCount").description("일자별 조회수 - 조회수"),
					fieldWithPath("viewCount.viewCountPerReferer[].referer").description("Referer별 조회수 - Referer"),
					fieldWithPath("viewCount.viewCountPerReferer[].viewCount").description("Referer별 조회수 - 조회수")
				)));
	}

	@Test
	@DisplayName("단축 URL 리다이렉트")
	void url_redirect() throws Exception {

		LocalDateTime currentDate = TimeUtil.getCurrentSeoulTime();
		Url url = Url.createWithoutShortenedUrl(
			"https://github.com/yijoo-1", Url.DEFAULT_EXPIRATION_PERIOD, currentDate);
		String shortenedUrl = "shortenedUrl";
		url.assignShortenedUrl(shortenedUrl);

		urlRepository.save(url);

		// when & then
		ResultActions resultActions = mockMvc.perform(
				get("/{shortenedUrl}", shortenedUrl))
			.andExpect(status().isMovedPermanently());

		// REST Docs
		resultActions
			.andDo(print())
			.andDo(document("short-url-redirect",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse(),
				pathParameters(
					parameterWithName("shortenedUrl").description("단축된 URL")
				)));
	}
}