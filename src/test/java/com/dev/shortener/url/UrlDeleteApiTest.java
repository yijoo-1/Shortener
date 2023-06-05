package com.dev.shortener.url;

import com.dev.shortener.domain.url.entity.Url;
import com.dev.shortener.domain.url.repository.UrlRepository;
import com.dev.shortener.domain.url.util.TimeUtil;
import com.dev.shortener.restdocs.ApiDocumentUtils;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Transactional
class UrlDeleteApiTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UrlRepository urlRepository;

	@Test
	@DisplayName("단축 URL 삭제")
	void url_delete() throws Exception {

		LocalDateTime currentDate = TimeUtil.getCurrentSeoulTime();
		Url url = Url.createWithoutShortenedUrl(
			"https://github.com/yijoo-1", Url.DEFAULT_EXPIRATION_PERIOD, currentDate.minusDays(7));
		String shortenedUrl = "shortenedUrl";
		url.assignShortenedUrl(shortenedUrl);

		Url savedUrl = urlRepository.save(url);

		// when & then
		ResultActions resultActions = mockMvc.perform(
				delete("/api/urls/{urlId}", savedUrl.id()))
			.andExpect(status().isNoContent());

		// REST Docs
		resultActions
			.andDo(print())
			.andDo(document("short-url-delete",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse(),
				pathParameters(
					parameterWithName("urlId").description("단축된 URL ID")
				)));
	}
}