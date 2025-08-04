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
            document.getElementById("content").textContent = notice.content;
            document.getElementById("writer").textContent = notice.userName || "작성자 정보 없음";
            document.getElementById("date").textContent = new Date(notice.createdAt).toLocaleDateString();
            document.getElementById("views").textContent = notice.views;
        })
        .catch(err => {
            console.error("공지 단건 조회 실패:", err);
        });
});
