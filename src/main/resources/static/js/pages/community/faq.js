document.addEventListener("DOMContentLoaded", function () {
    let currentPage = 0; // 0부터 시작
    const pageSize = 15;

    const list = document.getElementById("faq-list");

    // ✅ 제목 셀 클릭 시 단건 조회 이동 (이벤트 위임 방식)
    list.addEventListener("click", function (event) {
        const target = event.target;
        if (target && target.getAttribute("data-field") === "title") {
            const row = target.closest("tr");
            const id = row.dataset.id;
            window.location.href = `/view/faq/${id}`;
        }
    });

    function fetchFaq(page) {

        const url = `/api/faq?page=${page}&size=${pageSize}&sort=DESC`;
        console.log("Sending GET request to:", url);

        fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(responseData => {
                console.log(responseData);
                list.innerHTML = "";

                const faq = responseData.data.content; // Page 객체의 content
                faq.forEach((faq, index) => {
                    const createdAtTime = new Date(faq.createdAt).toLocaleDateString();

                    const row = document.createElement('tr');
                    row.dataset.id = faq.id;

                    row.innerHTML = `
                        <td>${page * pageSize + index + 1}</td>
                        <td data-field="title" style="cursor:pointer;">${faq.title}</td>
                        <td data-field="name" style="text-align: left;">${faq.userName || '작성자 정보 없음'}</td>
                        <td data-field="createdAt">${createdAtTime}</td>
                    `;

                    list.appendChild(row);
                });

                renderPagination(responseData.data);
            })
            .catch(error => {
                console.error('자주묻는질문 조회 실패:', error);
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
        prevBtn.addEventListener("click", () => fetchFaq(currentPage - 1)); // fetchFaq로 수정
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
        nextBtn.addEventListener("click", () => fetchFaq(currentPage + 1)); // fetchFaq로 수정
        pagination.appendChild(nextBtn);
    }

    fetchFaq(currentPage); // 첫 로딩 시 fetchFaq 호출
});
