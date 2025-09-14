package com.example.smallwaxing.notice.controller;

import com.example.smallwaxing.global.common.SuccessResponse;
import com.example.smallwaxing.global.security.Login;
import com.example.smallwaxing.notice.dto.*;
import com.example.smallwaxing.notice.service.NoticeService;
import com.example.smallwaxing.user.dto.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor //final 필드나 @NonNull이 붙은 필드에 대해 생성자를 자동으로 생성해주는 기능
@RequestMapping("/api") // 올바른 위치
public class NoticeController {

    private final NoticeService noticeService;

    //생성
    @PostMapping(value = "/notice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<Void> createNotice(@Login LoginUser loginUser,
                                              @ModelAttribute @Valid NoticeCreateRequest createDto){

        noticeService.createNotice(loginUser, createDto);

        return SuccessResponse.<Void>builder()
                .status(HttpStatus.CREATED.value())
                .message("공지사항 생성 성공")
                .build();
    }

    //수정
    @PutMapping(value = "/notice/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse<Void> updateNotice(@PathVariable Long id,
                                              @Login LoginUser loginUser,
                                              @ModelAttribute @Valid NoticeUpdateRequest updateDto) {

        noticeService.updateNotice(loginUser, id, updateDto);

        return SuccessResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("공지사항 수정 성공")
                .build();
    }

    // 공지 삭제
    @DeleteMapping("/notice/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SuccessResponse<Void> deleteNotice(@Login LoginUser loginUser, @PathVariable Long id) {
        noticeService.deleteNotice(loginUser, id); // 작성자 또는 ADMIN 권한 확인은 서비스에서 수행

        return SuccessResponse.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("공지 삭제 성공")
                .build();
    }

    //공지 전체 조회
    @GetMapping("/notice")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse<Page<NoticeFindAllResponse>> findAllNotice(@ModelAttribute NoticePaging noticePaging) {

        return SuccessResponse.<Page<NoticeFindAllResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("공지 조회 성공")
                .data(noticeService.findAllNotices(noticePaging))  // 바로 호출 결과 넣기 가능
                .build();
    }

    //공지 단건 조회
    @GetMapping("/notice/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponse<NoticeResponse> getNotice(@PathVariable Long id) {
        return SuccessResponse.<NoticeResponse>builder()
                .status(200)
                .data(noticeService.findNotice(id))
                .message("공지 단건 조회 성공")
                .build();
    }
}
