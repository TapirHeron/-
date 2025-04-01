// 初始化图表
function initChart() {
    const ctx = document.getElementById('detectionChart').getContext('2d');
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['周一','周二','周三','周四','周五','周六','周日'],
            datasets: [{
                label: '检测数量',
                data: [65, 59, 80, 81, 56, 55, 40],
                borderColor: getComputedStyle(document.documentElement).getPropertyValue('--primary-color').trim(),
                tension: 0.4,
                fill: true,
                backgroundColor: 'rgba(91, 143, 249, 0.05)'
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false }
            }
        }
    });
}

// 检测功能
function startTextDetection() {
    document.getElementById('detectContent').placeholder = "请输入需要检测的文本...";
}

function startLinkDetection() {
    document.getElementById('detectContent').placeholder = "请输入需要检测的链接...";
}

async function analyzeContent() {
    const btn = document.querySelector('.analysis-btn');
    btn.disabled = true;
    btn.style.opacity = 0.8;
    
    // 模拟检测过程
    await new Promise(resolve => setTimeout(resolve, 2000));
    
    // 显示结果
    showResultModal();
    btn.disabled = false;
    btn.style.opacity = 1;
}

// 初始化
document.addEventListener('DOMContentLoaded', () => {
    initChart();
    // 卡片入场动画
    document.querySelectorAll('.card').forEach((card, index) => {
        card.style.animationDelay = `${index * 0.1}s`;
    });
});