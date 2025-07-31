package com.example.smallwaxing.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 20)
    private String userNum;  // 아이디

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

    public User(String userNum, String password, String userName, String pNum, String address, Role role) {
        this.userNum = userNum;
        this.password = password;
        this.userName = userName;
        this.pNum = pNum;
        this.address = address;
        this.role = role;
    }
}
