package com.example.smallwaxing.notice.service;

import com.example.smallwaxing.global.error.exception.NoticeNotFoundException;
import com.example.smallwaxing.image.domain.Image;
import com.example.smallwaxing.image.service.ImageService;
import com.example.smallwaxing.notice.domain.Notice;
import com.example.smallwaxing.global.error.exception.UserNotFoundException;
import com.example.smallwaxing.notice.dto.*;
import com.example.smallwaxing.notice.repository.NoticeRepository;
import com.example.smallwaxing.user.domain.User;
import com.example.smallwaxing.user.dto.LoginUser;
import com.example.smallwaxing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    //공지생성
    @Transactional
    public void createNotice(LoginUser loginUser, NoticeCreateRequest dto) {
        User user = userRepository.findByUserNumAndRole(loginUser.getUserNum(), loginUser.getRole())
                .orElseThrow(UserNotFoundException::new);

        Notice notice = dto.toEntity(user);

        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            for (MultipartFile file : dto.getImages()) {
                Image img = imageService.save(file);
                if (img != null) notice.addImage(img);
            }
        }
        noticeRepository.save(notice); // cascade로 Image 함께 저장
    }

    //공지 업데이트
    @Transactional
    public void updateNotice(LoginUser loginUser, Long id, NoticeUpdateRequest request) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));

        // 작성자 검증
        if (!notice.getUser().getUserNum().equals(loginUser.getUserNum())) {
            throw new SecurityException("본인이 작성한 공지만 수정할 수 있습니다.");
        }

        // 1. 텍스트 필드 업데이트
        request.applyTo(notice);

        // 2. 삭제할 이미지 처리
        if (request.getRemoveImages() != null) {
            for (String path : request.getRemoveImages()) {
                Image toRemove = notice.getImages().stream()
                        .filter(img -> img.getFilePath().equals(path))
                        .findFirst()
                        .orElse(null);
                if (toRemove != null) {
                    notice.removeImage(toRemove);
                    imageService.deleteFile(toRemove);
                }
            }
        }

        // 3. 새 이미지 추가
        if (request.getImages() != null) {
            for (MultipartFile file : request.getImages()) {
                if (!file.isEmpty()) {
                    Image image = imageService.save(file);
                    notice.addImage(image);
                }
            }
        }
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

