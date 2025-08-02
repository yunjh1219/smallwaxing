package com.example.smallwaxing.notice.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeFindAllResponse {

    private Long id; //아이디
    private String noticeTitle; //제목
    private String noticeContent; //내용
    private LocalDateTime createdAt; // 생성일
    private LocalDateTime updatedAt; // 수정일
    private String userName; // 유저 이름

    @Builder
    @QueryProjection
    public NoticeFindAllResponse(Long id, String noticeTitle, String noticeContent, LocalDateTime createdAt, LocalDateTime updatedAt, String userName) {
        this.id = id;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userName = userName;
    }

}
