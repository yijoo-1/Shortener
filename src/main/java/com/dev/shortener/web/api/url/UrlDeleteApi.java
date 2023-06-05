package com.dev.shortener.web.api.url;


import com.dev.shortener.domain.url.service.UrlDeleter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlDeleteApi {

	private final UrlDeleter urlDeleter;

	public UrlDeleteApi(UrlDeleter urlDeleter) {
		this.urlDeleter = urlDeleter;
	}

	@DeleteMapping("/api/urls/{urlId}")
	public ResponseEntity<Void> delete(@PathVariable Long urlId) {
		urlDeleter.delete(urlId);

		return ResponseEntity.noContent()
			.build();
	}
}
