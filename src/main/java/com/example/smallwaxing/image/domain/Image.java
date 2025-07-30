package com.example.smallwaxing.image.domain;

import com.example.smallwaxing.notice.domain.Notice;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    private String filename; // 실제 파일명 혹은 저장명
    private String filepath; // 저장 경로
    private LocalDateTime uploadedAt;

    // 필요 시 이미지 유형, 용량 등 추가 가능
}
