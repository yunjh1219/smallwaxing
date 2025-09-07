document.addEventListener("DOMContentLoaded", function () {
    let currentPage = 0; // 0부터 시작
    const pageSize = 15;

    const list = document.getElementById("notice-list");

    // ✅ 제목 셀 클릭 시 단건 조회 이동 (이벤트 위임 방식)
    list.addEventListener("click", function (event) {
        const target = event.target;
        if (target && target.getAttribute("data-field") === "title") {
            const row = target.closest("tr");
            const id = row.dataset.id;
            window.location.href = `/view/notice/${id}`;
        }
    });

    function fetchNotices(page) {
        fetch(`/api/notice?page=${page}&size=${pageSize}&sort=DESC`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(responseData => {
                list.innerHTML = "";

                const notices = responseData.data.content; // Page 객체의 content
                notices.forEach((notice, index) => {
                    const createdAtTime = new Date(notice.createdAt).toLocaleDateString();

                    const row = document.createElement('tr');
                    row.dataset.id = notice.id;

                    row.innerHTML = `
                        <td>${page * pageSize + index + 1}</td>
                        <td data-field="title" style="cursor:pointer;">${notice.title}</td>
                        <td data-field="name" style="text-align: left;">${notice.userName || '작성자 정보 없음'}</td>
                        <td data-field="createdAt">${createdAtTime}</td>
                    `;

                    list.appendChild(row);
                });

                renderPagination(responseData.data);
            })
            .catch(error => {
                console.error('공지 조회 실패:', error);
            });
    }

    function renderPagination(pageData) {
        const pagination = document.getElementById("pagination");
        pagination.innerHTML = "";

        const totalPages = pageData.totalPages;
        const currentPage = pageData.number;

        // 이전 버튼
        const prevBtn = document.createElement("button");
        prevBtn.textContent = "<";
        prevBtn.disabled = currentPage === 0;
        prevBtn.addEventListener("click", () => fetchNotices(currentPage - 1));
        pagination.appendChild(prevBtn);

        // 현재 페이지 (1부터 시작)
        const currentBtn = document.createElement("button");
        currentBtn.textContent = currentPage + 1;
        currentBtn.disabled = true;
        currentBtn.style.backgroundColor = "#333"; // 원하는 스타일 적용
        currentBtn.style.color = "#fff";
        pagination.appendChild(currentBtn);

        // 다음 버튼
        const nextBtn = document.createElement("button");
        nextBtn.textContent = ">";
        nextBtn.disabled = currentPage >= totalPages - 1;
        nextBtn.addEventListener("click", () => fetchNotices(currentPage + 1));
        pagination.appendChild(nextBtn);
    }

    fetchNotices(currentPage); // 첫 로딩 시
});


