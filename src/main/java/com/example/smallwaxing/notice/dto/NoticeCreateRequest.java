package com.example.smallwaxing.notice.dto;

import com.example.smallwaxing.notice.domain.Notice;
import com.example.smallwaxing.user.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Data
@NoArgsConstructor // 🔹 폼 데이터/JSON 매핑 위해 필요
public class NoticeCreateRequest {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    @NotBlank(message = "내용을 입력해주세요")
    private String content;
    private boolean isPinned; // 상단 고정 여부 (선택)

    // 🔹 이미지 파일 여러 개 받을 수 있도록
    private List<MultipartFile> images;

    public Notice toEntity(User user) {
        return  Notice.builder()
                .title(title)
                .content(content)
                .user(user)
                .isPinned(this.isPinned)
                .build();

    }

}