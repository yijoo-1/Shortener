package com.dev.shortener.web.util;

import javax.servlet.http.HttpServletRequest;

public class ServerAddressParser {

	private ServerAddressParser() {
	}

	public static String parseServerAddress(HttpServletRequest request) {
		return request.getServerName() + ":" + request.getServerPort();
	}
}
