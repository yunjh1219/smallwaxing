async function fetchWithAuth(url, options = {}) {
    const token = localStorage.getItem("jwtToken");
    if (!options.headers) {
        options.headers = {};
    }
    options.headers["Authorization"] = `Bearer ${token}`;

    let response = await fetch(url, options);

    // 액세스 토큰 만료 → 401
    if (response.status === 401) {
        console.warn("토큰 만료 → 재발급 시도");

        // 1. 리프레시 토큰으로 재발급 요청
        const reissueResponse = await fetch("/api/reissue", { method: "POST" });
        if (reissueResponse.ok) {
            // 2. 새 액세스 토큰을 응답 헤더에서 추출
            const newToken = reissueResponse.headers.get("Authorization")?.replace("Bearer ", "");
            if (newToken) {
                localStorage.setItem("jwtToken", newToken);

                // 3. 다시 요청 (retry)
                options.headers["Authorization"] = `Bearer ${newToken}`;
                response = await fetch(url, options);
            }
        } else {

            // 리프레시 토큰 만료 → 토큰 제거 + 로그인 페이지 이동
            localStorage.removeItem("jwtToken");

            // 리프레시 토큰도 만료 → 로그인 페이지로
            window.location.href = "/login";
        }
    }

    return response;
}
