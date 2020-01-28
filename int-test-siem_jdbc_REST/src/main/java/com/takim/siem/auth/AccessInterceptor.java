package com.takim.siem.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.takim.siem.constant.Constant;

public class AccessInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(AccessInterceptor.class);

	private TokenAuthManager jwtUtil = new TokenAuthManager();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// Request Header에서 토큰 가져오기
		String tokenString = request.getHeader(Constant.HEADER_AUTH);

		// Request 에서 클라이언트 IP 가져오기
		String requestIP = "";
		if (request.getHeader("X-FORWARDED-FOR") != null) {
			// proxy 또는 Cloud 환경에 있거나 방화벽에 인해 웹서버나 Load Balancer의 IP를 가져오지 않도록 헤더 먼저 체크
			requestIP = request.getHeader("X-FORWARDED-FOR");
		} else {
			requestIP = request.getRemoteAddr();
		}

		if (Constant.ALLOW_IP_LIST.contains(requestIP)) {
			// 요청하는 클라이언트의 IP가 접근 허용 IP일 때
			if (tokenString == null || tokenString.isEmpty()) {
				// 토큰이 없으면 생성

				String newToken = new TokenAuthManager().createJWT(requestIP);

				Cookie tokenCookie = new Cookie(Constant.COOKIE_NAME, newToken);
				tokenCookie.setMaxAge(60 * 60 * 24 * 365); // 1년
				tokenCookie.setDomain("");
				tokenCookie.setPath("/");
				tokenCookie.setHttpOnly(true);
				tokenCookie.setSecure(true);
				response.addCookie(tokenCookie);

				logger.info("Token 검증...Token 생성 완료 => 요청 IP : " + requestIP);

				return true;
			} else {
				// 토큰이 있으면 토큰 검증
				if (jwtUtil.isPermitted(tokenString, requestIP)) {
					return true;
				} else {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
					return false;
				}
			}
		} else {
			// 요청하는 클라이언트의 IP가 접근 미허용 IP일 때
			logger.info("Token 검증...접근 미허용 IP => 요청 IP : " + requestIP);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
		}
	}
}
