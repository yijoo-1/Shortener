package com.dev.shortener.web.api.docs;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class DocsApi {

	@GetMapping("/api/docs")
	public ResponseEntity<String> docs() {
		URI docsPath = URI.create("/docs/index.html");

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(docsPath);

		return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
	}
}
