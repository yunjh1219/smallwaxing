package com.example.smallwaxing.image.service;

import com.example.smallwaxing.image.domain.Image;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ImageService {

    // ✅ 공통 물리 경로 & URL 프리픽스 사용 (notice 고정 아님)
    @Value("${app.upload.physical-dir}")          // ✅
    private String physicalDir;

    @Value("${app.upload.url-prefix:/uploads/}")  // ✅
    private String urlPrefix;


    private static final Set<String> ALLOWED_EXT = Set.of("jpg","jpeg","png","gif","webp");
    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB

    /** 파일 저장 */
    public Image save(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        validate(file);

        try {
            // 디렉토리 보장 (한 폴더: uploads)
            Path dir = Paths.get(physicalDir);
            Files.createDirectories(dir);

            // 안전한 파일명
            String origName = Optional.ofNullable(file.getOriginalFilename()).orElse("unnamed");
            String ext = Optional.ofNullable(FilenameUtils.getExtension(origName)).orElse("").toLowerCase();
            String safeName = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);

            Path target = dir.resolve(safeName);
            file.transferTo(target.toFile());

            // 트랜잭션 롤백 시 저장 파일 제거
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override public void afterCompletion(int status) {
                    if (status == STATUS_ROLLED_BACK) {
                        try { Files.deleteIfExists(target); } catch (IOException ignored) {}
                    }
                }
            });

            // DB엔 웹 경로만 저장: /uploads/{uuid.ext}
            return Image.builder()
                    .fileName(origName)
                    .filePath(normalizeUrl(urlPrefix) + safeName)
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패: " + file.getOriginalFilename(), e);
        }
    }

    /** 실제 파일 삭제 */
    public void deleteFile(Image image) {
        if (image == null || !StringUtils.hasText(image.getFilePath())) return;

        String fileName = Paths.get(image.getFilePath()).getFileName().toString();
        Path target = Paths.get(physicalDir).resolve(fileName);

        try {
            Files.deleteIfExists(target);
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 실패: " + target, e);
        }
    }

    // --- 검증/유틸 ---
    private void validate(MultipartFile file) {
        if (file.getSize() > MAX_SIZE) throw new IllegalArgumentException("파일 용량 초과(최대 5MB)");
        String contentType = Optional.ofNullable(file.getContentType()).orElse("").toLowerCase();
        if (!contentType.startsWith("image/")) throw new IllegalArgumentException("이미지 파일만 업로드 가능");
        String ext = Optional.ofNullable(FilenameUtils.getExtension(file.getOriginalFilename()))
                .orElse("").toLowerCase();
        if (!ALLOWED_EXT.contains(ext)) throw new IllegalArgumentException("허용 확장자: " + ALLOWED_EXT);
    }

    private String normalizeUrl(String prefix) {
        return prefix.endsWith("/") ? prefix : prefix + "/";
    }
}
