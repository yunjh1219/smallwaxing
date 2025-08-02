package com.example.smallwaxing.notice.service;


import com.example.smallwaxing.notice.domain.Notice;
import com.example.smallwaxing.global.error.exception.UserNotFoundException;
import com.example.smallwaxing.notice.dto.NoticeCreateRequest;
import com.example.smallwaxing.notice.dto.NoticeFindAllResponse;
import com.example.smallwaxing.notice.repository.NoticeRepository;
import com.example.smallwaxing.user.domain.User;
import com.example.smallwaxing.user.dto.LoginUser;
import com.example.smallwaxing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    //공지생성
    @Transactional
    public void createNotice(LoginUser loginUser, NoticeCreateRequest createDto) {
        User user = userRepository.findByUserNumAndRole(loginUser.getUserNum(), loginUser.getRole())
                .orElseThrow(UserNotFoundException::new);

        Notice notice = createDto.toEntity(user);

        noticeRepository.save(notice);
    }

    //전체 조회
    public List<NoticeFindAllResponse> findAllNotice(){
        return noticeRepository.findAllNotice();
    }

}

