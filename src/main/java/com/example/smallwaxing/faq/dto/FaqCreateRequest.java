package com.example.smallwaxing.faq.dto;

import com.example.smallwaxing.faq.domain.Category;
import com.example.smallwaxing.faq.domain.Faq;
import com.example.smallwaxing.notice.domain.Notice;
import com.example.smallwaxing.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FaqCreateRequest {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    @NotBlank(message = "내용을 입력해주세요")
    private String content;
    @NotBlank(message = "카테고리를 선택해주세요")
    private Category category;

    public Faq toEntity(User user) {
        return  Faq.builder()
                .title(title)
                .content(content)
                .user(user)
                .category(category)
                .build();

    }
}
