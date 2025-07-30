package com.example.smallwaxing.notice.domain;

import com.example.smallwaxing.image.domain.Image;
import com.example.smallwaxing.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;       //작성자

    @OneToMany
    @JoinColumn(name = "notice_id")
    private List<Image> images; // 이미지

    @Column(nullable = false)
    private String title;    //제목

    @Column(nullable = false)
    private String content;  //내용

    @Column(nullable = false)
    private boolean isPinned; // 고정 여부

    private int views = 0; //조회수

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt; //생성일
    @UpdateTimestamp
    private LocalDateTime updatedAt; //수정일

}
