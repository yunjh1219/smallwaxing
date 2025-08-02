package com.example.smallwaxing.notice.repository;

import com.example.smallwaxing.notice.dto.NoticeFindAllResponse;

import java.util.List;
import java.util.Optional;

public interface NoticeRepositoryCustom {

    List<NoticeFindAllResponse> findAllNotice();

}
