// login.js
const rootIp = 'localhost';
async function register() {
    const btn = document.querySelector('.login-btn');
    btn.classList.add('loading');

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const userEmail = document.getElementById('email').value;
    const errorMessage = document.getElementById('error-message');

    // 清除状态
    errorMessage.style.display = 'none';
    document.querySelectorAll('input').forEach(i => i.style.borderColor = '#E0E0E0');

    try {
        // 模拟网络延迟
        await new Promise(resolve => setTimeout(resolve, 800));

        if (!username || !password) {
            showError('请填写完整登录信息！');
            return;
        } else if (username.length < 4 || password.length < 6 ) {
            showError('用户名长度至少为3，密码长度至少为6！');
            return;
        }

        const response = await fetch('http://' + rootIp + ':8080/user/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userName: username, userPassword: password, userEmail: userEmail})
        });

        const data = await response.json();
        if (data.status === 200) {
            showSuccess();
            // localStorage.setItem('token', data.data.token);
            setTimeout(() => window.location.href = '../html/main.html', 500);
        } else {
            showError(data.message || '验证失败，请检查输入');
        }
    } catch (error) {
        showError('网络连接异常，请稍后重试');
    } finally {
        btn.classList.remove('loading');
    }
}

function showError(message) {
    const errorMessage = document.getElementById('error-message');
    errorMessage.querySelector('.message-text').textContent = message;
    errorMessage.style.display = 'flex';
    errorMessage.classList.remove('error');
    void errorMessage.offsetWidth; // 触发重绘
    errorMessage.classList.add('error');

    // 输入框抖动动画
    document.querySelectorAll('input').forEach(input => {
        if (!input.value) {
            input.style.animation = 'shake 0.4s';
            setTimeout(() => input.style.animation = '', 400);
        }
    });
}

function showSuccess() {
    const success = document.getElementById('success-message');
    success.style.display = 'flex';
    success.classList.add('show');

    setTimeout(() => {
        success.style.display = 'none';
        success.classList.remove('show');
    }, 2000);
}


// 抖动动画
const shake = [
    { transform: 'translateX(0)' },
    { transform: 'translateX(10px)' },
    { transform: 'translateX(-10px)' },
    { transform: 'translateX(6px)' },
    { transform: 'translateX(-6px)' },
    { transform: 'translateX(0)' }
];

document.styleSheets[0].insertRule(
    `@keyframes shake { 
        0%, 100% { transform: translateX(0); }
        20% { transform: translateX(10px); }
        40% { transform: translateX(-10px); }
        60% { transform: translateX(6px); }
        80% { transform: translateX(-6px); }
    }`, 0);