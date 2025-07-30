package com.example.smallwaxing.global.filter;

import com.example.smallwaxing.global.common.FailResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class ApiAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		FailResponse error = FailResponse.builder()
			.status(HttpStatus.UNAUTHORIZED.value())
			.message("인증되지 않은 사용자 입니다.")
			.build();

		String json = objectMapper.writeValueAsString(error);

		setResponseProperties(response);
		writeJsonToResponse(response, json);
	}

	private void setResponseProperties(HttpServletResponse response) {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}

	private void writeJsonToResponse(HttpServletResponse response, String json) throws IOException {
		response.getWriter().write(json);
	}
}
