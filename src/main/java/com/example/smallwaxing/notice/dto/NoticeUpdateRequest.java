package com.example.smallwaxing.notice.dto;

import com.example.smallwaxing.notice.domain.Notice;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor // 🔹 폼 데이터/JSON 매핑 위해 필요
public class NoticeUpdateRequest {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    private boolean isPinned; // 상단 고정 여부 (선택)

    // 🔹 이미지 파일 여러 개 받을 수 있도록
    private List<MultipartFile> images;

    private List<String> removeImages; // 삭제할 파일 경로

    // 기존 Notice 수정하는 메서드
    public void applyTo(Notice notice) {
        notice.updateNotice(this.title, this.content, this.isPinned);
    }
}
