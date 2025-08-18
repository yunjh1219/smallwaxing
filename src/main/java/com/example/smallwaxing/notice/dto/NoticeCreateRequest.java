package com.example.smallwaxing.notice.dto;

import com.example.smallwaxing.notice.domain.Notice;
import com.example.smallwaxing.user.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("isPinned") // ✅ JSON 키 'isPinned'를 명시적으로 매핑
    private boolean isPinned; // 상단 고정 여부 (선택)

    public Notice toEntity(User user) {
        return  Notice.builder()
                .title(title)
                .content(content)
                .user(user)
                .isPinned(this.isPinned)
                .build();

    }

}