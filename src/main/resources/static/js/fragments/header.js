
// 스크롤 내릴 때 padding 제거되게 -----------------------------------------------------------------------------
window.addEventListener('scroll', function() {
    const gnb = document.querySelector('.header'); // .gnb 요소 선택
    const depth2 = document.querySelectorAll('.gnb_depth2');
    if (window.scrollY > 0) { // 스크롤이 0보다 클 경우
        gnb.classList.add('scrolled'); // 'scrolled' 클래스 추가
        depth2.classList.add('scrolled');
    } else {
        gnb.classList.remove('scrolled'); // 'scrolled' 클래스 제거
        depth2.classList.add('scrolled');
    }
});

// overlay -----------------------------------------------------------------------------
function toggleMenu(button) {
    const overlay = document.querySelector('.overlay');
    const isOpened = button.classList.toggle('opened');
    button.setAttribute('aria-expanded', isOpened);

    // overlay의 상태에 따라 클래스 추가 또는 제거
    if (isOpened) {
        overlay.classList.add('open'); // overlay 보이게 설정
    } else {
        overlay.classList.remove('open'); // overlay 숨김
    }
}


document.addEventListener("DOMContentLoaded", function () {
    const logoutButton = document.getElementById("logoutButton");

    const token = localStorage.getItem("jwtToken");

    if (token) {
        try {
            const payloadBase64 = token.split('.')[1];
            const decodedPayload = JSON.parse(atob(payloadBase64));

            const role = decodedPayload.role;

            if (role === 'ADMIN' || role === 'USER') {
                logoutButton.style.display = "inline-block";

                logoutButton.addEventListener("click", function () {
                    fetchWithAuth("/logout", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": `Bearer ${token}`
                        }
                    })
                        .then(response => {
                            if (response.ok) {
                                // 토큰 삭제
                                localStorage.removeItem("jwtToken");
                                alert("로그아웃 성공");
                                window.location.href = "/"; // 홈으로 리디렉션
                            } else {
                                alert("로그아웃 실패");
                            }
                        })
                        .catch(error => {
                            console.error("로그아웃 중 오류 발생:", error);
                            alert("서버 오류");
                        });
                });

            } else {
                logoutButton.style.display = "none";
            }

        } catch (error) {
            console.error("JWT 디코딩 오류:", error);
            logoutButton.style.display = "none";
        }
    } else {
        logoutButton.style.display = "none";
    }
});