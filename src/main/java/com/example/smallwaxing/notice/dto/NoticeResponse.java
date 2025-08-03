package com.example.smallwaxing.notice.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeResponse {

    private Long id; //아이디
    private String title; //제목
    private String content; //내용
    private LocalDateTime createdAt; // 생성일
    private LocalDateTime updatedAt; // 수정일
    private String userName; // 유저 이름


    @Builder
    @QueryProjection
    public NoticeResponse(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, String userName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userName = userName;
    }


}
