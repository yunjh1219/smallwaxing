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


//로그아웃
function isLoggedIn() {
    const token = localStorage.getItem("jwtToken");
    if (!token) return false;

    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const now = Math.floor(Date.now() / 1000);
        return payload.exp > now;
    } catch (e) {
        return false;
    }
}

function logout() {

    // 로컬 스토리지에서 JWT 토큰 가져오기
    const token = localStorage.getItem('jwtToken');

    fetch('/api/logout', {
        method: 'POST',
        headers: {


            'Content-Type': 'application/json',
            'Authorization': `${token}`  // JWT 토큰을 Authorization 헤더에 추가
        },
        credentials: 'same-origin', // 쿠키 포함
    })
        .then(response => {
            if (response.ok) {
                // 로그아웃 성공 시 처리 (예: 로그인 페이지로 리디렉션)
                alert('로그아웃 성공');
                localStorage.removeItem('jwtToken');
                window.location.href = '/'; // 로그인 페이지로 리디렉션
                // 로컬 스토리지에서 JWT 토큰 삭제
            } else {
                // 로그아웃 실패 시 처리
                alert('로그아웃 실패');
            }
        })
        .catch(error => {
            alert('로그아웃 중 오류 발생');
        });
}

document.addEventListener("DOMContentLoaded", function() {
    if (isLoggedIn()) {
        const logoutButton = document.getElementById("logoutButton");
        if (logoutButton) {
            logoutButton.style.display = "block";
            logoutButton.addEventListener("click", logout);
        }
    }
});