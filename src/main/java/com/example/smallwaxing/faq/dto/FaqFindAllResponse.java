package com.example.smallwaxing.faq.dto;

import com.example.smallwaxing.faq.domain.Category;
import com.example.smallwaxing.faq.domain.Faq;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@NoArgsConstructor
public class FaqFindAllResponse {

    private Long id; //아이디
    private String title; //제목
    private String content; //내용
    private LocalDateTime createdAt; // 생성일
    private LocalDateTime updatedAt; // 수정일
    private Category category; // 카테고리
    private String userName; // 유저 이름

    @Builder
    @QueryProjection
    public FaqFindAllResponse(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Category category, String userName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
        this.userName = userName;
    }
}
