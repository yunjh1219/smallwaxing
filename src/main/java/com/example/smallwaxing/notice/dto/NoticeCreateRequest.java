package com.example.smallwaxing.notice.dto;

import com.example.smallwaxing.notice.domain.Notice;
import com.example.smallwaxing.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class NoticeCreateRequest {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    public Notice toEntity(User user) {


        return  Notice.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();

    }

}