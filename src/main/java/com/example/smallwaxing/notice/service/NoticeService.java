package com.example.smallwaxing.notice.service;


import com.example.smallwaxing.global.error.exception.NoticeNotFoundException;
import com.example.smallwaxing.notice.domain.Notice;
import com.example.smallwaxing.global.error.exception.UserNotFoundException;
import com.example.smallwaxing.notice.dto.NoticeCreateRequest;
import com.example.smallwaxing.notice.dto.NoticeFindAllResponse;
import com.example.smallwaxing.notice.dto.NoticePaging;
import com.example.smallwaxing.notice.dto.NoticeResponse;
import com.example.smallwaxing.notice.repository.NoticeRepository;
import com.example.smallwaxing.user.domain.User;
import com.example.smallwaxing.user.dto.LoginUser;
import com.example.smallwaxing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    //공지삭제
    @Transactional
    public void deleteNotice(LoginUser loginUser, Long id) {
        User user = userRepository.findByUserNumAndRole(loginUser.getUserNum(), loginUser.getRole())
                .orElseThrow(UserNotFoundException::new);

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(NoticeNotFoundException::new);

        noticeRepository.delete(notice);
    }
    //공지 전체조회
    @Transactional(readOnly = true)
    public Page<NoticeFindAllResponse> findAllNotices(NoticePaging noticePaging) {
        Sort sort = Sort.by(Sort.Direction.fromString(noticePaging.getSort()), "id");
        Pageable pageable = PageRequest.of(noticePaging.getPage(), noticePaging.getSize(), sort);

        Page<Notice> noticePage = noticeRepository.findAll(pageable);
        Page<NoticeFindAllResponse> noticeResponses = noticePage.map(NoticeFindAllResponse::new);

        return noticeResponses;
    }
    //공지 단건조회
    @Transactional
    public NoticeResponse findNotice(Long id) {

        return noticeRepository.getNoticeById(id)
                .orElseThrow(NoticeNotFoundException::new);
    }


}

