document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('noticeWriteForm');

    form.addEventListener('submit', async function (event) {
        event.preventDefault();

        const title = (document.getElementById('noticeTitle').value || '').trim();
        const content = (document.getElementById('noticeContent').value || '').trim();
        const isPinned = document.getElementById('pinned')?.checked === true;

        if (!title) {
            alert('제목은 필수입니다.');
            document.getElementById('noticeTitle').focus();
            return;
        }

        const writeNoticeData = { title, content, isPinned };

        // ✅ 로컬스토리지에서 JWT 꺼내오기
        const token = localStorage.getItem('jwtToken');

        fetch('/api/notice', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                ...(token ? { 'Authorization': `Bearer ${token}` } : {})
            },
            body: JSON.stringify(writeNoticeData)
        })
            .then(response => {
                // ✅ 서버에서 JSON 내려주므로 그대로 파싱
                return response.json();
            })
            .then(data => {
                if (data.status === 201) {   // 서버에서 HttpStatus.CREATED.value() == 201
                    alert('공지 작성 성공하였습니다.');
                    window.location.href = '/view/notice';
                } else {
                    alert('공지 작성 실패하였습니다.');
                }
            })
            .catch(error => {
                console.error('공지 작성 중 오류 발생:', error);
                alert('서버와의 연결에 실패했습니다. 나중에 다시 시도해주세요.');
            });
    });
});
