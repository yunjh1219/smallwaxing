package com.example.smallwaxing.notice.controller;

import com.example.smallwaxing.global.common.SuccessResponse;
import com.example.smallwaxing.global.security.Login;
import com.example.smallwaxing.notice.dto.NoticeCreateRequest;
import com.example.smallwaxing.notice.dto.NoticeFindAllResponse;
import com.example.smallwaxing.notice.service.NoticeService;
import com.example.smallwaxing.user.dto.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor //final 필드나 @NonNull이 붙은 필드에 대해 생성자를 자동으로 생성해주는 기능
@RequestMapping("/api") // 올바른 위치
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/notice")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> createNotice(@Login LoginUser loginUser, @RequestBody @Valid NoticeCreateRequest createDto){

        noticeService.createNotice(loginUser, createDto);

        return SuccessResponse.<Void>builder()
                .status(HttpStatus.CREATED.value())
                .message("공지사항 생성 성공")
                .build();
    }

    //공지 전체 조회
    @GetMapping("/notice")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse<List<NoticeFindAllResponse>> findAllNotice() {

        return SuccessResponse.<List<NoticeFindAllResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("공지 조회 성공")
                .data(noticeService.findAllNotice())
                .build();
    }

}
