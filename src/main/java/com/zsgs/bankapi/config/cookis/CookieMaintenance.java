package com.zsgs.bankapi.config.cookis;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CookieMaintenance {

	public void setJwtCookie(HttpServletResponse response, String jwtToken) {

		System.out.println(jwtToken + " setTocken");
		ResponseCookie cookie = ResponseCookie.from("jwt", jwtToken)
				.path("/")
				.httpOnly(true)
				// .secure(true)
				// .sameSite("None")
				.maxAge(86400)
				.build();

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}

	public void clearJwtCookie(HttpServletResponse response) {
		ResponseCookie cookie = ResponseCookie.from("jwt", null)
				.path("/")
				.httpOnly(true)
				// .secure(true)
				// .sameSite("None")
				.maxAge(0)
				.build();

		response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
	}

}
