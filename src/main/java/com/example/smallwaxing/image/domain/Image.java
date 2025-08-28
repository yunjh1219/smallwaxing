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
    private Long id;

    private String filename;
    private String filepath;

    private String targetType; // "NOTICE", "EVENT", "REVIEW" 등
    private Long targetId;     // 연결된 엔티티의 PK
}