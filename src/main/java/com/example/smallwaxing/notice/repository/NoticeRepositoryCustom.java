package com.example.smallwaxing.notice.repository;

import com.example.smallwaxing.notice.dto.NoticeFindAllResponse;
import com.example.smallwaxing.notice.dto.NoticeResponse;

import java.util.List;
import java.util.Optional;

public interface NoticeRepositoryCustom {

    List<NoticeFindAllResponse> findAllNotice();
    Optional<NoticeResponse> getNoticeById(Long id);
}
