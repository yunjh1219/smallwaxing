document.addEventListener("DOMContentLoaded", function () {
    // 현재 URL에서 id 추출
    const path = window.location.pathname; // "/view/notice/3"
    const parts = path.split("/");
    const id = parts[parts.length - 1]; // "3"

    fetch(`/api/notice/${id}`)
        .then(res => res.json())
        .then(data => {
            const notice = data.data;

            document.getElementById("title").textContent = notice.title;
            document.getElementById("writer").textContent = notice.userName || "작성자 정보 없음";
            document.getElementById("date").textContent = new Date(notice.createdAt).toLocaleDateString();
            document.getElementById("views").textContent = notice.views;

            const contentDiv = document.getElementById("content");
            contentDiv.innerHTML = ""; // 초기화

            // ✅ 본문 텍스트
            const textP = document.createElement("p");
            textP.textContent = notice.content;
            contentDiv.appendChild(textP);

            // ✅ 이미지들
            if (notice.imageUrls && notice.imageUrls.length > 0) {
                notice.imageUrls.forEach(url => {
                    const img = document.createElement("img");
                    img.src = url;
                    img.alt = "공지 이미지";
                    img.style.maxWidth = "100%";  // 반응형
                    img.style.marginTop = "10px";
                    contentDiv.appendChild(img);
                });
            }
        })
        .catch(err => {

            console.error("공지 단건 조회 실패:", err);
        });


});
