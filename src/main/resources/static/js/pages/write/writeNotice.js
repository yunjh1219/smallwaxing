document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('noticeWriteForm');
    const fileInput = document.getElementById('image');
    const fileListContainer = document.getElementById('fileList');
    const fileWarning = document.getElementById('file-warning');

    const allowedExtensions = ["jpg", "jpeg", "png", "gif", "webp"];
    let selectedFiles = []; // ✅ 선택된 파일들을 따로 저장

    // 파일 선택 시
    fileInput.addEventListener("change", function () {
        const newFiles = Array.from(fileInput.files);

        // 새로 선택한 파일을 기존 selectedFiles에 추가
        newFiles.forEach(file => {
            const ext = file.name.split(".").pop().toLowerCase();

            // 파일 검증
            if (file.size > 10 * 1024 * 1024) {
                fileWarning.textContent = `⚠ ${file.name}은(는) 10MB를 초과합니다.`;
                fileWarning.style.display = "block";
                return;
            }
            if (!allowedExtensions.includes(ext)) {
                fileWarning.textContent = `⚠ ${file.name}은(는) 허용되지 않는 파일 형식입니다.`;
                fileWarning.style.display = "block";
                return;
            }

            // 중복 방지 (같은 이름 파일 두 번 추가 안 되게)
            if (!selectedFiles.some(f => f.name === file.name && f.size === file.size)) {
                selectedFiles.push(file);
            }
        });

        // 최대 3개 제한
        if (selectedFiles.length > 3) {
            fileWarning.textContent = "⚠ 최대 3개까지 업로드할 수 있습니다.";
            fileWarning.style.display = "block";
            selectedFiles = selectedFiles.slice(0, 3); // 앞 3개만 유지
        } else {
            fileWarning.style.display = "none";
        }

        // 목록 갱신
        renderFileList();
        fileInput.value = ""; // input 비워줘야 다시 선택 가능
    });

    // 파일 목록 렌더링
    function renderFileList() {
        fileListContainer.innerHTML = "";
        selectedFiles.forEach((file, index) => {
            const item = document.createElement("div");
            item.textContent = `${index + 1}. ${file.name} (${(file.size / 1024).toFixed(1)}KB)`;

            // 삭제 버튼
            const removeBtn = document.createElement("button");
            removeBtn.textContent = "❌";
            removeBtn.style.marginLeft = "10px";
            removeBtn.addEventListener("click", () => {
                selectedFiles.splice(index, 1);
                renderFileList();
            });

            item.appendChild(removeBtn);
            fileListContainer.appendChild(item);
        });

        // input[type=text]에도 표시
        document.getElementById("imageInput").value = selectedFiles.map(f => f.name).join(", ");
    }

    // 폼 전송
    form.addEventListener('submit', async function (event) {
        event.preventDefault();

        const formData = new FormData(form);

        // checkbox 값 맞추기
        formData.set("isPinned", document.getElementById('pinned').checked);

        // 선택된 파일 배열에서 추가
        selectedFiles.forEach(file => formData.append("images", file));

        console.log("=== 전송할 FormData 값 ===");
        for (let [key, value] of formData.entries()) {
            if (value instanceof File) {
                console.log(key, `${value.name} (${(value.size / 1024).toFixed(1)}KB)`);
            } else {
                console.log(key, value);
            }
        }

        const token = localStorage.getItem('jwtToken');

        try {
            const response = await fetch('/api/notice', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`
                },
                body: formData
            });

            if (!response.ok) throw new Error("저장 중 오류");

            alert("공지사항이 성공적으로 저장되었습니다.");
            window.location.href = '/view/notice';

        } catch (err) {
            console.error(err);
            alert("공지사항 저장 실패: " + err.message);
        }
    });
});
