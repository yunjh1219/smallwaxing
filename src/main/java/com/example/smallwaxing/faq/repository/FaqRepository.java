package com.example.smallwaxing.faq.repository;

import com.example.smallwaxing.faq.domain.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq,Long>, FaqRepositoryCustom {
}
