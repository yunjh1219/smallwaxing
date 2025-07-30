package com.example.smallwaxing.notice.service;

import com.example.smallwaxing.notice.repository.NoticeRepository;
import com.example.smallwaxing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
}

