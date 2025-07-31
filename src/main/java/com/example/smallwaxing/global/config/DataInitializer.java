package com.example.smallwaxing.global.config;

import com.example.smallwaxing.user.domain.Role;
import com.example.smallwaxing.user.domain.User;
import com.example.smallwaxing.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Setter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Setter
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) { // DB에 계정이 없을 때만 실행
            User admin = new User("admin", passwordEncoder.encode("admin1234"), "관리자", "010-0000-0000", "서울시", Role.ADMIN);
            admin.setUserNum("admin");
            admin.setPassword(passwordEncoder.encode("admin1234")); // 비밀번호 암호화 필수!
            admin.setRole(Role.ADMIN); // 역할 설정 (Role enum 또는 문자열)

            userRepository.save(admin);

            System.out.println("초기 관리자 계정(admin / admin1234)이 생성되었습니다.");
        }

    }
}