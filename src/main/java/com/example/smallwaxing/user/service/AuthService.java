package com.example.smallwaxing.user.service;

import com.example.smallwaxing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;


}
