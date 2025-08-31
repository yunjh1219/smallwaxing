package com.example.smallwaxing.notice.domain;

import com.example.smallwaxing.image.domain.Image;
import com.example.smallwaxing.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Column(nullable = false)
    private String title;    //제목

    @Column(nullable = false)
    private String content;  //내용

    @Column(nullable = false)
    private boolean isPinned; // 고정 여부

    private int views = 0; //조회수

    @CreationTimestamp
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime createdAt; //생성일

    @UpdateTimestamp
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime updatedAt; //수정일

    @Builder
    public Notice(User user, String title, String content, boolean isPinned) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.isPinned = isPinned;
    }

    /** 🔹 편의 메서드 추가 */
    public void addImage(Image image) {
        images.add(image);
        image.setNotice(this); // 양방향 관계 동기화
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setNotice(null); // 관계 끊기
    }

}
