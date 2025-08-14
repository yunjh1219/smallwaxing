package com.example.smallwaxing.faq.repository;

import com.example.smallwaxing.faq.domain.Category;
import com.example.smallwaxing.faq.dto.FaqFindAllResponse;
import com.example.smallwaxing.faq.dto.FaqResponse;
import com.example.smallwaxing.faq.dto.FaqSearchCondition;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.smallwaxing.faq.domain.QFaq.faq;


@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqRepositoryCustomImpl implements FaqRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FaqFindAllResponse> findAllFaq(FaqSearchCondition condition, Pageable pageable) {
        List<FaqFindAllResponse> content = queryFactory
                .select(Projections.constructor(FaqFindAllResponse.class,
                        faq.id,
                        faq.title,
                        faq.content,
                        faq.createdAt,
                        faq.updatedAt,
                        faq.category,
                        faq.user.userName
                ))
                .from(faq)
                .where(categoryEq(condition.getCategory())) // ✅ 여기서 조건 처리
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(faq.count())
                .from(faq)
                .where(categoryEq(condition.getCategory())) // ✅ 동일한 조건으로 count
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Optional<FaqResponse> getFaqById(Long id) {
        FaqResponse fetchOne = queryFactory
                .select(Projections.constructor(FaqResponse.class,
                        faq.id,
                        faq.content
                ))
                .from(faq)
                .where(faq.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(fetchOne);
    }


    private BooleanExpression categoryEq(String category) {
        return category == null ? null : faq.category.eq(Category.of(category));
    }


}