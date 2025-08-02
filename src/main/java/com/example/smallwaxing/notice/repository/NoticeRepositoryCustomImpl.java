package com.example.smallwaxing.notice.repository;


import com.example.smallwaxing.notice.dto.NoticeFindAllResponse;
import com.example.smallwaxing.notice.dto.QNoticeFindAllResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.smallwaxing.notice.domain.QNotice.notice;
import static com.example.smallwaxing.user.domain.QUser.user;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<NoticeFindAllResponse> findAllNotice() {
        return queryFactory
                .select(new QNoticeFindAllResponse(
                        notice.id,
                        notice.title,
                        notice.content,
                        notice.createdAt,
                        notice.updatedAt,
                        notice.user.userName
                ))
                .from(notice)
                .leftJoin(notice.user, user)
                .fetch(); // 조건 없이 전체 조회
    }

}