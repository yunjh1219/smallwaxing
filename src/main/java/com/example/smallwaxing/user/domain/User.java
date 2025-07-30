package com.example.smallwaxing.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private Integer userNum;  // 아이디

    @Column(nullable = false, length = 20)
    private String password;  // 비밀번호

    @Column(nullable = false, length = 20)
    private String userName;  // 이름

    @Column(nullable = false, length = 20)
    private String pNum;  // 연락처

    @Column(nullable = false, length = 30)
    private String address;  // 주소

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt; //생성일

    @UpdateTimestamp
    private LocalDateTime updateAt; //수정일

    @Enumerated(EnumType.STRING)
    private Role role; //권한
}
