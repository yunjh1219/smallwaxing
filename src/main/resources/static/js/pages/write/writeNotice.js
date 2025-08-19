//공지 작성
document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('noticeWriteForm');

    form.addEventListener('submit', async function (event) {
        event.preventDefault();

        const noticeTitle = (document.getElementById('noticeTitle').value || '').trim();
        const noticeContent = (document.getElementById('noticeContent').value || '').trim();
        const isPinned = document.getElementById('pinned')?.checked === true;

        const NoticeData = {
            title: noticeTitle,
            content: noticeContent,
            isPinned: isPinned
        };

        // ✅ 로컬스토리지에서 JWT 꺼내오기
        const token = localStorage.getItem('jwtToken');

        fetch('/api/notice', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify(NoticeData)
        })
            .then(response => {
                if (response.ok) {
                    alert('새로운 공지사항이 성공적으로 저장되었습니다.');
                    window.location.href = '/view/notice';
                } else {
                    throw new Error('저장 중 오류가 발생했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('공지사항 정보를 저장하는 중 오류가 발생했습니다.');
            });
    });
});
