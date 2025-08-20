package com.example.smallwaxing.user.service;

import com.example.smallwaxing.global.error.exception.InvalidSigningInformation;
import com.example.smallwaxing.global.error.exception.InvalidTokenException;
import com.example.smallwaxing.global.error.exception.UserNotFoundException;
import com.example.smallwaxing.global.security.JwtProvider;
import com.example.smallwaxing.global.security.RefreshToken;
import com.example.smallwaxing.global.security.Token;
import com.example.smallwaxing.user.domain.User;
import com.example.smallwaxing.user.dto.LoginRequestDto;
import com.example.smallwaxing.user.dto.LoginUser;
import com.example.smallwaxing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    //로그인
    @Transactional
    public Token login(LoginRequestDto loginRequestDto) {
        String userNum = loginRequestDto.getUserNum();
        String password = loginRequestDto.getPassword();

        User user = userRepository.findByUserNum(userNum)
                .orElseThrow(InvalidSigningInformation::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidSigningInformation();
        }

        Token token = jwtProvider.createToken(user.getUserNum(), user.getRole());

        user.updateRefreshToken(token.getRefreshToken().getData());

        System.out.println("✅ 로그인 성공 : userNum=" + user.getUserNum()
                + ", role=" + user.getRole()
                + ", accessToken=" + token.getAccessToken().getData());

        return token;
    }

    //로그아웃
    @Transactional
    public void logout(LoginUser loginUser) {
        User user = userRepository.findByUserNum(loginUser.getUserNum())
                .orElseThrow(UserNotFoundException::new);

        user.invalidateRefreshToken();
    }

    //토큰 재발급
    @Transactional
    public Token reissue(RefreshToken refreshToken) {
        String refreshTokenValue = refreshToken.getData();

        if (!jwtProvider.isTokenValid(refreshTokenValue)) {
            throw new InvalidTokenException();
        }

        User user = userRepository.findByRefreshToken(refreshTokenValue)
                .orElseThrow(UserNotFoundException::new);
        Token token = jwtProvider.createToken(user.getUserNum(), user.getRole());

        user.updateRefreshToken(token.getRefreshToken().getData());
        System.out.println("✅ 토큰 재발급 성공 : " + token.getAccessToken().getData());
        return token;
    }

}
