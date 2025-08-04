package com.example.smallwaxing.faq.controller;

import com.example.smallwaxing.faq.dto.FaqCreateRequest;
import com.example.smallwaxing.faq.dto.FaqFindAllResponse;
import com.example.smallwaxing.faq.dto.FaqPaging;
import com.example.smallwaxing.faq.dto.FaqSearchCondition;
import com.example.smallwaxing.faq.service.FaqService;
import com.example.smallwaxing.global.common.SuccessResponse;
import com.example.smallwaxing.global.security.Login;

import com.example.smallwaxing.user.dto.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FaqController {

    private final FaqService faqService;

    @PostMapping("/faq")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> createFaq(@Login LoginUser loginUser, @RequestBody @Valid FaqCreateRequest createDto){

        faqService.createFaq(loginUser, createDto);

        return SuccessResponse.<Void>builder()
                .status(HttpStatus.CREATED.value())
                .message("FAQ 생성 성공")
                .build();
    }

    @GetMapping("/faq")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse<Page<FaqFindAllResponse>> findAllFaq(@ModelAttribute FaqPaging faqPaging, FaqSearchCondition cond) {

        return SuccessResponse.<Page<FaqFindAllResponse>>builder()
                .status(HttpStatus.OK.value())
                .data(faqService.findAllFaq(faqPaging,cond))  // 바로 호출 결과 넣기 가능
                .message("FAQ 조회 성공")
                .build();
    }



}
