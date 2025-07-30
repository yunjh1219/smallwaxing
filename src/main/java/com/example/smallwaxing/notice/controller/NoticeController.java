package com.example.smallwaxing.notice.controller;

import com.example.smallwaxing.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor //final 필드나 @NonNull이 붙은 필드에 대해 생성자를 자동으로 생성해주는 기능
public class NoticeController {

    private final NoticeService noticeService;

}
