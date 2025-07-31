package com.example.smallwaxing.user.service;

import com.example.smallwaxing.global.error.exception.InvalidSigningInformation;
import com.example.smallwaxing.global.security.JwtProvider;
import com.example.smallwaxing.global.security.Token;
import com.example.smallwaxing.user.domain.User;
import com.example.smallwaxing.user.dto.LoginRequestDto;
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

        return token;
    }

}
