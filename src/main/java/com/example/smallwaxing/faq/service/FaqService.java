package com.example.smallwaxing.faq.service;

import com.example.smallwaxing.faq.domain.Faq;
import com.example.smallwaxing.faq.dto.FaqCreateRequest;
import com.example.smallwaxing.faq.dto.FaqFindAllResponse;
import com.example.smallwaxing.faq.dto.FaqPaging;
import com.example.smallwaxing.faq.dto.FaqSearchCondition;
import com.example.smallwaxing.faq.repository.FaqRepository;
import com.example.smallwaxing.global.error.exception.FaqNotFoundException;
import com.example.smallwaxing.global.error.exception.UserNotFoundException;

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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqService {

    private final FaqRepository faqRepository;
    private final UserRepository userRepository;

    //자주묻는질문 생성
    @Transactional
    public void createFaq(LoginUser loginUser, FaqCreateRequest createDto) {
        User user = userRepository.findByUserNumAndRole(loginUser.getUserNum(), loginUser.getRole())
                .orElseThrow(UserNotFoundException::new);

        Faq faq = createDto.toEntity(user);

        faqRepository.save(faq);
    }

    //전체조회 + 컨디션 조회
    @Transactional(readOnly = true)
    public Page<FaqFindAllResponse> findAllFaq(FaqPaging faqPaging, FaqSearchCondition condition) {
        System.out.println("📌 [category 값 확인] condition.getCategory() = '" + condition.getCategory() + "'");
        Sort sort = Sort.by(Sort.Direction.fromString(faqPaging.getSort()), "id");
        Pageable pageable = PageRequest.of(faqPaging.getPage(), faqPaging.getSize(), sort);

        // FAQ 엔티티를 반환받는 것이 아니라 DTO를 직접 반환
        return faqRepository.findAllFaq(condition, pageable);
    }

}
