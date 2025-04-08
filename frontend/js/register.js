const rootIp = 'localhost';
function register() {
    const userDto = {
        userName: document.getElementById('username').value,
        userPassword: document.getElementById('password').value,
        userEmail: document.getElementById('email').value,
    }
    fetch('http://' + rootIp + ':8080/user/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userDto)
    }).then(response => response.json())
        .then(data => {
            if (data.code === 200) {
                window.location.href = '../html/main.html'
            }
        })
}