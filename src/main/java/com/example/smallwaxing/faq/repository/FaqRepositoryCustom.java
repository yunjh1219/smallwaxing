package com.example.smallwaxing.faq.repository;


import com.example.smallwaxing.faq.dto.FaqFindAllResponse;
import com.example.smallwaxing.faq.dto.FaqSearchCondition;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable; // ✅ Spring Data JPA의 Pageable



public interface FaqRepositoryCustom {

    Page<FaqFindAllResponse> findAllFaq(FaqSearchCondition condition, Pageable pageable);


}
