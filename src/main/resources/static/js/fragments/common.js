function reissueToken() {
    const token = localStorage.getItem('jwtToken');  // 로컬 스토리지에서 JWT 토큰 가져오기

    if (!token) {
        console.error('토큰이 없습니다. 로그인 후 다시 시도해주세요.');
        return;
    }

    fetch('/api/reissue', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`  // JWT 토큰을 Authorization 헤더에 포함
        }
    })
        .then(response => {
            if (response.ok) {
                console.log('토큰 재발급 성공');
            } else {
                console.error('토큰 재발급 실패');
            }
        })
        .catch(err => console.error('네트워크 오류:', err));
}

// 25분(1500초)마다 실행
setInterval(reissueToken, 1500000);


