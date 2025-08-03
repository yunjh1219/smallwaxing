package com.example.smallwaxing.notice.dto;

import com.example.smallwaxing.notice.domain.Notice;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Getter
@NoArgsConstructor
public class NoticeFindAllResponse {

    private Long id; //아이디
    private String title; //제목
    private String content; //내용
    private LocalDateTime createdAt; // 생성일
    private LocalDateTime updatedAt; // 수정일
    private String userName; // 유저 이름
    private boolean isPinned; // 고정 여부
    private int views = 0; //조회수

    @Builder
    @QueryProjection
    public NoticeFindAllResponse(Long id, String title, String content,
                                 LocalDateTime createdAt, LocalDateTime updatedAt,
                                 boolean isPinned, int views, String userName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isPinned = isPinned;
        this.views = views;
        this.userName = userName;
    }

    // Notice 엔티티를 받는 변환용 생성자 추가
    public NoticeFindAllResponse(Notice notice) {
        this.id = notice.getId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.createdAt = notice.getCreatedAt();
        this.updatedAt = notice.getUpdatedAt();
        this.isPinned = notice.isPinned();
        this.views = notice.getViews();
        this.userName = notice.getUser().getUserName();  // User 엔티티에서 username 필드 추출
    }

}
