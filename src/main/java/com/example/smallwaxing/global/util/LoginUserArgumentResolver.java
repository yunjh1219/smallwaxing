package com.example.smallwaxing.global.util;

import com.example.smallwaxing.global.error.exception.InvalidLoginUserException;
import com.example.smallwaxing.global.filter.CustomUserDetails;
import com.example.smallwaxing.global.security.Login;
import com.example.smallwaxing.user.domain.Role;
import com.example.smallwaxing.user.dto.LoginUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
		boolean hasLoginUserRole = LoginUser.class.isAssignableFrom(parameter.getParameterType());

		return hasLoginAnnotation && hasLoginUserRole;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) throw new InvalidLoginUserException();

		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();



		return LoginUser.builder()
			.userNum(customUserDetails.getUsername())
			.role(customUserDetails.getRole())
			.build();
	}
}
