document.addEventListener('DOMContentLoaded', async function () {
    const form = document.getElementById('noticeWriteForm');
    const fileInput = document.getElementById('image');
    const fileListContainer = document.getElementById('fileList');
    const fileWarning = document.getElementById('file-warning');
    const url = new URL(window.location.href);
    const id = url.searchParams.get("id"); // 수정 모드 체크
    const allowedExtensions = ["jpg", "jpeg", "png", "gif", "webp"];
    let selectedFiles = [];

    // 수정 모드일 때 기존 데이터 불러오기
    if (id) {
        try {
            const res = await fetchWithAuth(`/api/notice/${id}`);
            if (!res.ok) throw new Error("데이터 조회 실패");
            const data = await res.json();

            // 입력값 채우기
            document.getElementById("title").value = data.title;
            document.getElementById("content").value = data.content;
            document.getElementById("pinned").checked = data.isPinned;

            // 서버에 저장된 파일도 목록에 표시 (삭제 불가 예시)
            if (data.images && data.images.length > 0) {
                data.images.forEach(img => {
                    const item = document.createElement("div");
                    item.textContent = `📎 ${img}`;
                    fileListContainer.appendChild(item);
                });
            }
        } catch (err) {
            console.error(err);
            alert("데이터 불러오기 실패: " + err.message);
        }
    }

    // 파일 선택
    fileInput.addEventListener("change", function () {
        const newFiles = Array.from(fileInput.files);
        newFiles.forEach(file => {
            const ext = file.name.split(".").pop().toLowerCase();

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

            if (!selectedFiles.some(f => f.name === file.name && f.size === file.size)) {
                selectedFiles.push(file);
            }
        });

        if (selectedFiles.length > 3) {
            fileWarning.textContent = "⚠ 최대 3개까지 업로드할 수 있습니다.";
            fileWarning.style.display = "block";
            selectedFiles = selectedFiles.slice(0, 3);
        } else {
            fileWarning.style.display = "none";
        }

        renderFileList();
        fileInput.value = "";
    });

    // 파일 목록 렌더링
    function renderFileList() {
        fileListContainer.innerHTML = "";
        selectedFiles.forEach((file, index) => {
            const item = document.createElement("div");
            item.textContent = `${index + 1}. ${file.name} (${(file.size / 1024).toFixed(1)}KB)`;

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

        document.getElementById("imageInput").value = selectedFiles.map(f => f.name).join(", ");
    }

    // 폼 전송 (작성 or 수정)
    form.addEventListener('submit', async function (event) {
        event.preventDefault();
        const formData = new FormData(form);
        formData.set("isPinned", document.getElementById('pinned').checked);
        selectedFiles.forEach(file => formData.append("images", file));

        const token = localStorage.getItem('jwtToken');

        try {
            const response = await fetchWithAuth(
                id ? `/api/notice/${id}` : '/api/notice',
                {
                    method: id ? 'PUT' : 'POST',
                    headers: { 'Authorization': `Bearer ${token}` },
                    body: formData
                }
            );

            if (!response.ok) throw new Error("저장 중 오류");
            alert(id ? "공지사항이 수정되었습니다." : "공지사항이 성공적으로 저장되었습니다.");
            window.location.href = '/view/notice';

        } catch (err) {
            console.error(err);
            alert("공지사항 저장 실패: " + err.message);
        }
    });
});
