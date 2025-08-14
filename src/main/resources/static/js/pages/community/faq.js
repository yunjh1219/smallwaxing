document.addEventListener("DOMContentLoaded", function () {
    let currentPage = 0; // 0부터 시작
    const pageSize = 15;

    const list = document.getElementById("faq-list");

    // 상세 내용 캐시 (id -> { content, fetched })
    const faqCache = new Map();

    // 한 번에 하나만 열고 싶으면 true
    const SINGLE_OPEN = true;

    // ✅ 제목 클릭 시: 상세 아코디언 토글
    list.addEventListener("click", async function (event) {
        const target = event.target;
        if (!target || target.getAttribute("data-field") !== "title") return;

        const row = target.closest("tr");
        const id = row?.dataset.id;
        if (!id) return;

        // 이미 상세행이 있으면 토글만
        let detailRow = row.nextElementSibling;
        const isDetailRow = detailRow && detailRow.classList.contains("faq-detail");

        // 한 번에 하나만 열기: 다른 열린 것 닫기
        if (SINGLE_OPEN) {
            closeAllDetailsExcept(row);
        }

        if (isDetailRow) {
            // 토글만
            toggleDetail(detailRow);
            return;
        }

        // 상세행 없으면 생성
        detailRow = document.createElement("tr");
        detailRow.className = "faq-detail";
        const td = document.createElement("td");
        td.colSpan = 4; // 테이블 컬럼 수에 맞게 조정
        td.innerHTML = `
          <div class="faq-detail-content" id="faq-detail-${id}">
            <div class="faq-detail-inner">불러오는 중...</div>
          </div>
        `;
        detailRow.appendChild(td);
        row.after(detailRow);

        // 데이터 준비 (캐시 우선)
        if (!faqCache.has(id)) {
            try {
                const res = await fetch(`/api/faq/${id}`, {
                    method: 'GET',
                    headers: { 'Content-Type': 'application/json' }
                });
                const data = await res.json();
                // 백엔드 응답 구조에 맞게 속성명 조정 (예: data.content)
                const content = (data?.data?.content) ?? (data?.content) ?? '내용이 없습니다.';
                faqCache.set(id, { content, fetched: true });
            } catch (e) {
                faqCache.set(id, { content: '내용을 불러오지 못했습니다.', fetched: true });
            }
        }

        // 내용 주입
        const container = document.getElementById(`faq-detail-${id}`);
        const inner = container.querySelector('.faq-detail-inner');
        inner.innerHTML = sanitizeHTML(faqCache.get(id).content); // HTML 내려오면 그대로, 텍스트면 textContent 권장

        // 열기 애니메이션
        openSlide(container);
    });

    function fetchFaq(page) {
        const url = `/api/faq?page=${page}&size=${pageSize}&sort=DESC`;

        fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(responseData => {
                list.innerHTML = "";

                const faq = responseData.data.content; // Page 객체의 content
                faq.forEach((item, index) => {
                    const createdAtTime = new Date(item.createdAt).toLocaleDateString();

                    const row = document.createElement('tr');
                    row.dataset.id = item.id;

                    row.innerHTML = `
                    <td>${page * pageSize + index + 1}</td>
                    <td data-field="title" class="faq-title">${item.title}</td>
                    <td data-field="name" style="text-align: left;">${item.userName || '작성자 정보 없음'}</td>
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

        const prevBtn = document.createElement("button");
        prevBtn.textContent = "<";
        prevBtn.disabled = currentPage === 0;
        prevBtn.addEventListener("click", () => fetchFaq(currentPage - 1));
        pagination.appendChild(prevBtn);

        const currentBtn = document.createElement("button");
        currentBtn.textContent = currentPage + 1;
        currentBtn.disabled = true;
        currentBtn.style.backgroundColor = "#333";
        currentBtn.style.color = "#fff";
        pagination.appendChild(currentBtn);

        const nextBtn = document.createElement("button");
        nextBtn.textContent = ">";
        nextBtn.disabled = currentPage >= totalPages - 1;
        nextBtn.addEventListener("click", () => fetchFaq(currentPage + 1));
        pagination.appendChild(nextBtn);
    }

    // --- 아코디언 유틸 ---
    function openSlide(el) {
        el.classList.add('open');
        el.style.maxHeight = el.scrollHeight + 'px';
    }
    function closeSlide(el) {
        el.style.maxHeight = el.scrollHeight + 'px'; // 현재 높이로 고정
        requestAnimationFrame(() => {
            el.classList.remove('open');
            el.style.maxHeight = '0px';
        });
    }
    function toggleDetail(detailRow) {
        const box = detailRow.querySelector('.faq-detail-content');
        if (!box) return;
        if (box.classList.contains('open')) closeSlide(box);
        else openSlide(box);
    }
    function closeAllDetailsExcept(baseRow) {
        // 모든 faq-detail 닫기 (baseRow 바로 다음의 상세행은 제외)
        const all = list.querySelectorAll('.faq-detail .faq-detail-content.open');
        all.forEach(el => {
            // baseRow 다음이 상세행인 경우 그건 유지
            const dr = baseRow?.nextElementSibling;
            const isBaseDetail = dr && dr.contains(el);
            if (!isBaseDetail) closeSlide(el);
        });
        // 닫힌 뒤 DOM을 깔끔히 하고 싶으면, 애니메이션 끝난 뒤 tr 제거(옵션)
        setTimeout(() => {
            list.querySelectorAll('.faq-detail .faq-detail-content:not(.open)').forEach(el => {
                const tr = el.closest('tr.faq-detail');
                if (tr) tr.remove();
            });
        }, 260);
    }

    // (옵션) 간단한 XSS 방지 — 백엔드에서 HTML을 내려주지 않고 순수 텍스트라면 이 함수 대신 textContent를 쓰세요.
    function sanitizeHTML(html) {
        // 백엔드가 신뢰되는 환경이면 그대로 반환 가능. 필요 시 정교한 sanitizer로 교체.
        return html;
    }

    fetchFaq(currentPage);
});
