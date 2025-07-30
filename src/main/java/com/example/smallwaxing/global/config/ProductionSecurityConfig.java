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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Profile("prod")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ProductionSecurityConfig {

	private final ApiUserDetailsService userDetailsService;
	private final ApiAuthenticationEntryPoint entryPoint;
	private final ApiAccessDeniedHandler deniedHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable()) // CSRF 비활성화
			.cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 설정
			.headers(headers -> headers.frameOptions(frame -> frame.disable())) // H2 콘솔 등에서 사용 시 Frame 옵션 비활성화
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(ApiUrls.PERMIT_API_URLS).permitAll() // 특정 URL 접근 허용
				.anyRequest().authenticated() // 그 외 요청 인증 필요
			)
			.exceptionHandling(exceptions -> exceptions
				.authenticationEntryPoint(entryPoint) // 인증 실패 처리
				.accessDeniedHandler(deniedHandler) // 권한 부족 처리
			);


		return http.build();
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		config.setAllowedOrigins(Arrays.asList("*"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE"));
		config.setAllowedHeaders(Arrays.asList("*"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return source;
	}
}
