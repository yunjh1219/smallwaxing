package com.example.smallwaxing.notice.repository;

import com.example.smallwaxing.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long>, NoticeRepositoryCustom {

}