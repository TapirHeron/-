// 初始化图表
const rootIp = 'localhost';
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

let currentDetectionType = 'text'; // 当前检测类型：text/image
let selectedFile = null; // 存储选中的文件

// 检测功能
function startTextDetection() {
    currentDetectionType = 'text';
    document.getElementById('detectContent').style.display = 'block';
    document.getElementById('imageUploadArea').style.display = 'none';
    clearFileSelection();
}

function startPictureDetection() {
    currentDetectionType = 'image';
    document.getElementById('detectContent').style.display = 'none';
    document.getElementById('imageUploadArea').style.display = 'block';
    setupDragAndDrop();
}

// 新增文件处理函数
function setupDragAndDrop() {
    const uploadArea = document.getElementById('imageUploadArea');
    
    // 点击选择文件
    document.getElementById('imageInput').addEventListener('change', handleFileSelect);
    
    // 拖拽功能
    uploadArea.addEventListener('dragover', (e) => {
        e.preventDefault();
        uploadArea.classList.add('dragover');
    });
    
    uploadArea.addEventListener('dragleave', () => {
        uploadArea.classList.remove('dragover');
    });
    
    uploadArea.addEventListener('drop', (e) => {
        e.preventDefault();
        uploadArea.classList.remove('dragover');
        const files = e.dataTransfer.files;
        if (files.length > 0) handleFileSelect({ target: { files } });
    });
}

// 处理文件选择
function handleFileSelect(e) {
    const file = e.target.files[0];
    if (!file) return;

    // 文件类型验证
    if (!file.type.startsWith('image/')) {
        showError('仅支持图片文件 (JPG/PNG)');
        return;
    }

    // 文件大小限制 (2MB)
    if (file.size > 2 * 1024 * 1024) {
        showError('图片大小不能超过2MB');
        return;
    }

    selectedFile = file;
    previewImage(file);
}

// 预览图片
function previewImage(file) {
    const reader = new FileReader();
    reader.onload = (e) => {
        document.getElementById('imagePreview').innerHTML = `
            <div class="preview-container">
                <img src="${e.target.result}" alt="预览图片">
                <button onclick="clearFileSelection()" class="remove-btn">
                    <i class="fas fa-times"></i>
                </button>
            </div>
        `;
    };
    reader.readAsDataURL(file);
}

// 清除文件选择
function clearFileSelection() {
    selectedFile = null;
    document.getElementById('imageInput').value = '';
    document.getElementById('imagePreview').innerHTML = '';
}

//分析函数
async function analyzeContent() {
    const btn = document.querySelector('.analysis-btn');
    const errorEl = document.getElementById('errorMessage');
    
    // 输入验证
    if (currentDetectionType === 'text') {
        const text = document.getElementById('detectContent').value.trim();
        if (!text) {
            showError('请输入需要检测的文本');
            return;
        }
    } else if (!selectedFile) {
        showError('请选择需要检测的图片');
        return;
    }

    // 禁用按钮
    btn.disabled = true;
    btn.querySelector('.wave-loader').style.display = 'block';
    errorEl.style.display = 'none';

    try {
        let response;
        if (currentDetectionType === 'text') {
            // 文本检测请求
            response = await fetch(`http://${rootIp}:8080/api/text-analysis`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    content: document.getElementById('detectContent').value
                })
            });
        } else {
            // 图片检测请求
            const formData = new FormData();
            formData.append('image', selectedFile);
            
            response = await fetch(`http://${rootIp}:8080/api/image-analysis`, {
                method: 'POST',
                body: formData
            });
        }

        if (!response.ok) throw new Error(`HTTP错误: ${response.status}`);
        
        const result = await response.json();
        showResultModal(result);
    } catch (error) {
        showError(`分析失败: ${error.message}`);
    } finally {
        btn.disabled = false;
        btn.querySelector('.wave-loader').style.display = 'none';
    }
}


// 初始化
document.addEventListener('DOMContentLoaded', () => {
    initChart();
    // 卡片入场动画
    document.querySelectorAll('.card').forEach((card, index) => {
        card.style.animationDelay = `${index * 0.1}s`;
    });
});

// 错误提示
function showError(message) {
    const errorEl = document.getElementById('errorMessage');
    errorEl.textContent = message;
    errorEl.style.display = 'block';
}