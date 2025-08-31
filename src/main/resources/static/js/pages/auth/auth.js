//로그인
document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('loginForm');

    form.addEventListener('submit', function (event) {
        event.preventDefault(); // 폼 기본 제출 막기

        const userNum = document.getElementById('userNum').value;
        const password = document.getElementById('password').value;

        const loginData = {
            userNum: userNum,
            password: password
        };

        fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        })
            .then(response => {
                // 응답 헤더에서 JWT 토큰 추출
                const token = response.headers.get('Authorization'); // "Bearer <token>"

                if (token) {
                    // 로컬 스토리지에 JWT 토큰 저장 (Bearer 제거)
                    localStorage.setItem('jwtToken', token.replace('Bearer ', ''));
                }

                return response.json();
            })
            .then(data => {
                console.log('서버 응답:', data);

                if (data.status === 200) {
                    alert("로그인에 성공하였습니다.");
                    window.location.href = "/"; // 로그인 성공 시 이동할 페이지
                } else {
                    alert("로그인 실패! 아이디와 비밀번호를 확인해주세요.");
                }
            })
            .catch(error => {
                console.error('로그인 중 오류 발생:', error);
                alert('서버와의 연결에 실패했습니다. 나중에 다시 시도해주세요.');
            });
    });
});

document.addEventListener("DOMContentLoaded", function() {
    const logoutButton = document.getElementById("logoutButton");
    const loginButton = document.getElementById("loginButton");

    if (isLoggedIn()) {
        // 로그인 상태 → 로그아웃 버튼만 보임
        if (logoutButton) {
            logoutButton.style.display = "block";
            logoutButton.addEventListener("click", logout);
        }
        if (loginButton) loginButton.style.display = "none";

    } else {
        // 비로그인 상태 → 로그인 버튼만 보임
        if (logoutButton) logoutButton.style.display = "none";
        if (loginButton) loginButton.style.display = "block";
    }
});


function isLoggedIn() {
    const token = localStorage.getItem("jwtToken");
    if (!token) return false;

    try {
        // JWT payload 디코딩
        const payload = JSON.parse(atob(token.split(".")[1]));
        const exp = payload.exp * 1000; // 초 → 밀리초 변환
        return Date.now() < exp; // 만료 전이면 true
    } catch (e) {
        console.error("토큰 파싱 오류:", e);
        return false;
    }
}