package com.example.smallwaxing.image.domain;

import com.example.smallwaxing.notice.domain.Notice;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;   // 원본 파일명
    private String filePath;   // 저장된 경로 (예: /uploads/notice/xxx.png)

    // 공지사항과 연결 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @Builder   // 🔹 원하는 생성자에만 Builder 적용
    public Image(String fileName, String filePath, Notice notice) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.notice = notice;
    }

}