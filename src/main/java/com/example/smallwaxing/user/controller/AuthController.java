package com.example.smallwaxing.user.controller;

import com.example.smallwaxing.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


}
