package com.example.smallwaxing.global.config;

import com.example.smallwaxing.global.filter.ApiAccessDeniedHandler;
import com.example.smallwaxing.global.filter.ApiAuthenticationEntryPoint;
import com.example.smallwaxing.user.service.ApiUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Profile({"local", "default"})
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class LocalSecurityConfig {

	private final ApiUserDetailsService userDetailsService;
	private final ApiAuthenticationEntryPoint entryPoint;
	private final ApiAccessDeniedHandler deniedHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.formLogin(form -> form.disable()) // 폼 로그인 비활성화
			.httpBasic(basic -> basic.disable()) // HTTP Basic 인증 비활성화
			.csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
			.headers(headers -> headers
				.frameOptions(frame -> frame.disable())) // X-Frame-Options 비활성화
			.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless 세션 정책
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(toH2Console()).permitAll() // H2 콘솔 접근 허용
				.requestMatchers(ApiUrls.PERMIT_API_URLS).permitAll() // 특정 URL 허용
				.anyRequest().authenticated()) // 나머지 요청은 인증 필요
			.exceptionHandling(exceptions ->
				exceptions
					.authenticationEntryPoint(entryPoint) // 인증 실패 처리
					.accessDeniedHandler(deniedHandler) // 권한 부족 처리
			);


		return http.build();
	}




	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
