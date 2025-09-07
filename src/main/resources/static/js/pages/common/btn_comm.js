document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem("jwtToken");
    if (!token) return;

    try {
        const payloadBase64 = token.split('.')[1];
        const decodedPayload = JSON.parse(atob(payloadBase64));
        const role = decodedPayload.role;

        if (role === 'ADMIN') {
            // ✅ 모든 admin-write-btn 요소를 보여주기
            const adminBtns = document.querySelectorAll(".admin-btn");
            adminBtns.forEach(btn => {
                btn.style.display = "inline-block";

                // ✨ 여기서 data-action 읽어서 동작 연결
                const action = btn.dataset.action;
                btn.addEventListener("click", function () {
                    handleAdminAction(action);
                });
            });
        }
    } catch (err) {
        console.error("토큰 해석 실패:", err);
    }
});

function handleAdminAction(action) {
    const resData = getResourceFromUrl(); // switch 밖에서 선언

    switch (action) {
        //작성 페이지 이동
        case "write":
            if (!resData) return alert("이동할 페이지를 찾을 수 없습니다.");
            window.location.href = `/view/write/${resData.resource}`;
            break;

        //삭제
        case "delete":
            if (!resData) return alert("삭제할 리소스를 찾을 수 없습니다.");

            if (confirm("정말 삭제하시겠습니까?")) {
                fetchWithAuth(`/api/${resData.resource}/${resData.id}`, { method: "DELETE" })
                    .then(res => {
                        if (!res.ok) throw new Error("삭제 실패");
                        alert(`${resData.resource} 삭제 완료`);
                        window.location.href = `/view/${resData.resource}`;
                    })
                    .catch(err => alert(err.message));
            }
            break;

        default:
            console.warn("알 수 없는 action:", action);
    }
}


function getResourceFromUrl() {
    const url = new URL(window.location.href);
    const parts = url.pathname.split("/").filter(Boolean);

    const resource = parts[parts.length - 1]; // 기본적으로 마지막을 resource로 가정
    const maybeId = parts[parts.length - 1];
    const maybeResource = parts[parts.length - 2];

    if (!isNaN(maybeId)) {
        // URL이 /view/notice/3 형태라면
        return { resource: maybeResource, id: maybeId };
    }

    // URL이 /view/notice 형태라면
    return { resource, id: null };
}
