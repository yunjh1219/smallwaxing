package com.example.smallwaxing.notice.service;

import com.example.smallwaxing.global.error.exception.NoticeNotFoundException;
import com.example.smallwaxing.image.domain.Image;
import com.example.smallwaxing.notice.domain.Notice;
import com.example.smallwaxing.global.error.exception.UserNotFoundException;
import com.example.smallwaxing.notice.dto.NoticeCreateRequest;
import com.example.smallwaxing.notice.dto.NoticeFindAllResponse;
import com.example.smallwaxing.notice.dto.NoticePaging;
import com.example.smallwaxing.notice.dto.NoticeResponse;
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

    //공지생성
    @Transactional
    public void createNotice(LoginUser loginUser, NoticeCreateRequest createDto) {
        User user = userRepository.findByUserNumAndRole(loginUser.getUserNum(), loginUser.getRole())
                .orElseThrow(UserNotFoundException::new);

        Notice notice = createDto.toEntity(user);

        // 3) 이미지가 같이 넘어온 경우만 처리
        if (createDto.getImages() != null && !createDto.getImages().isEmpty()) {
            for (MultipartFile file : createDto.getImages()) {
                if (!file.isEmpty()) {
                    try {
                        // 3-1) 저장할 서버 경로 지정
                        String uploadDir = System.getProperty("user.dir") + "/uploads/notice/";
                        File dir = new File(uploadDir);
                        if (!dir.exists() && !dir.mkdirs()) {
                            throw new IOException("업로드 디렉토리 생성 실패: " + uploadDir);
                        }

                        // 3-2) 파일명 안전하게 생성 (UUID + 확장자)
                        String ext = FilenameUtils.getExtension(file.getOriginalFilename());
                        String safeFileName = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
                        String filePath = uploadDir + safeFileName;

                        // 3-3) 실제 파일 저장
                        file.transferTo(new File(filePath));

                        // 3-4) Image 엔티티 생성 (DB에는 웹에서 접근할 수 있는 URL만 저장)
                        Image image = Image.builder()
                                .fileName(file.getOriginalFilename())
                                .filePath("/uploads/notice/" + safeFileName)
                                .build();

                        // 3-5) Notice와 Image 연결 (양방향 관계)
                        notice.addImage(image);

                    } catch (IOException e) {
                        throw new RuntimeException("이미지 저장 실패: " + file.getOriginalFilename(), e);
                    }
                }
            }
        }

        // 🔹 Notice 저장 (cascade 때문에 Image도 함께 저장됨)
        noticeRepository.save(notice);
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

