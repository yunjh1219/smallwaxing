package com.example.smallwaxing.user.controller;

import com.example.smallwaxing.global.common.SuccessResponse;
import com.example.smallwaxing.global.security.AccessToken;
import com.example.smallwaxing.global.security.Login;
import com.example.smallwaxing.global.security.RefreshToken;
import com.example.smallwaxing.global.security.Token;
import com.example.smallwaxing.user.dto.LoginRequestDto;
import com.example.smallwaxing.user.dto.LoginUser;
import com.example.smallwaxing.user.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //로그인
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse<Void> signIn(@RequestBody @Valid LoginRequestDto loginDto, HttpServletResponse response) {
        Token token = authService.login(loginDto);

        setAccessToken(response, token.getAccessToken());
        setRefreshToken(response, token.getRefreshToken()); //리프레쉬 토큰 쿠키에 저장

        return SuccessResponse.<Void>builder()
                .status(200)
                .message("로그인 성공")
                .build();
    }

    //로그아웃
    @PostMapping("/api/logout")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse<Void> logout(@Login LoginUser loginUser, HttpServletResponse response) {
        authService.logout(loginUser);

        removeCookie(response);

        return SuccessResponse.<Void>builder()
                .status(200)
                .message("로그아웃 성공")
                .build();
    }



    private void setAccessToken(HttpServletResponse response, AccessToken accessToken) {
        setHeader(response, accessToken.getHeader(), accessToken.getData());
    }

    //리프레쉬 토큰 쿠키에 저장
    private void setRefreshToken(HttpServletResponse response, RefreshToken refreshToken) {
        Cookie cookie = createCookie(refreshToken.getHeader(), refreshToken.getData());
        response.addCookie(cookie);
    }
    private Cookie createCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);              // 자바스크립트 접근 차단 (XSS 방지)
        cookie.setSecure(true);                // HTTPS에서만 전송 (MITM 방지)
        cookie.setPath("/");
        cookie.setMaxAge(RefreshToken.EXPIRATION_PERIOD);
        cookie.setHttpOnly(true);
        return cookie;
    }

    //쿠키삭제
    private void removeCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("Refresh-Token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
    private void setHeader(HttpServletResponse response, String header, String data) {
        response.setHeader(header, data);
    }



}
