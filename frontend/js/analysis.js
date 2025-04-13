document.addEventListener('DOMContentLoaded', () => {
    // ======================
    // 初始化配置
    // ======================
    const CSS_VARS = {
        success: getComputedStyle(document.documentElement).getPropertyValue('--success-color').trim(),
        danger: getComputedStyle(document.documentElement).getPropertyValue('--danger-color').trim(),
        primary: getComputedStyle(document.documentElement).getPropertyValue('--primary-color').trim()
    };

    // ======================
    // DOM元素引用（带安全校验）
    // ======================
    const elements = {
        inputText: getElementWithCheck('inputText'),
        inputImage: getElementWithCheck('inputImage'),
        confidenceChart: getElementWithCheck('confidenceChart'),
        evidenceKeywords: getElementWithCheck('evidenceKeywords'),
        verdictBadge: getElementWithCheck('verdictBadge'),
        meterBar: getElementWithCheck('meterBar'),
        meterText: getElementWithCheck('meterText'),
        evidenceList: getElementWithCheck('evidenceList'),
        multiclsBars: getElementWithCheck('multiclsBars'),
        tamperOverlay: getElementWithCheck('tamperOverlay'),
        textTamper: getElementWithCheck('textTamper'),
        loadingOverlay: getElementWithCheck('loadingOverlay')
    };

    // ======================
    // 主流程
    // ======================
    try {
        // 1. 获取并验证数据
        const analysisData = loadAnalysisData();

        // 2. 展示基础内容
        renderBasicContent(analysisData);
        
        // 3. 初始化可视化
        initDataVisualization(analysisData.result);
        
        // 4. 隐藏加载状态
        elements.loadingOverlay.style.display = 'none';
        document.querySelector('.dashboard-grid').classList.add('loaded');
        
    } catch (error) {
        handleAnalysisError(error);
    }

    // ======================
    // 核心功能函数
    // ======================
    function loadAnalysisData() {
        const rawData = sessionStorage.getItem('analysisData');
        if (!rawData) throw new Error('找不到分析数据');
        
        const data = JSON.parse(rawData);
        if (!validateDataStructure(data)) throw new Error('无效的数据格式');

        console.log('分析数据:', data);
        
        return data;
    }

    function renderBasicContent(data) {
        // 文本内容
        elements.inputText.textContent = data.text || '无文本内容';
        

        // 图片处理
        if (data.image && data.image.startsWith('data:image')) {
            elements.inputImage.innerHTML = `
            <img src="${data.image}" 
                alt="分析用上传图片" 
                style="max-width: 100%; border-radius: 8px;">
            `;
        } else {
            elements.inputImage.innerHTML = '<div class="error">无效的图片数据</div>';
        }

    }

    function initDataVisualization(result) {
        renderRealFakeAnalysis(result.logits_real_fake);
        renderMulticlsBars(result.logits_multicls);
        renderTamperOverlay(result.output_coord);
        highlightTextTamper(result.logits_tok, result.original_text);
        updateEvidenceDisplay(result);
    }

    // ======================
    // 可视化模块
    // ======================
    function renderRealFakeAnalysis(logits) {
        const softmax = calculateSoftmax(logits);
        const confidence = Math.round(softmax[0] * 100);

        // 更新可信度指示器
        elements.meterBar.style.width = `${confidence}%`;
        elements.meterText.textContent = `${confidence}% 可信度`;
        elements.verdictBadge.className = `verdict-badge ${confidence > 50 ? 'real' : 'fake'}`;
        elements.verdictBadge.textContent = confidence > 50 ? '真实内容' : '检测到伪造';

        // 初始化饼图
        new Chart(elements.confidenceChart, getChartConfig(softmax));
    }

    function renderMulticlsBars(logits) {
        const labels = ['格式伪造 (FS)', '内容伪造 (FA)', '文本篡改 (TS)', '时间篡改 (TA)'];
        const sigmoid = logits.map(x => 1 / (1 + Math.exp(-x)));
        
        elements.multiclsBars.innerHTML = labels.map((label, i) => `
            <div class="type-bar">
                <span class="type-label">${label}</span>
                <div class="bar-container">
                    <div class="bar-fill" 
                         style="width: ${sigmoid[i] * 100}%;
                         background: ${CSS_VARS.primary}"></div>
                    <span class="bar-value">${(sigmoid[i] * 100).toFixed(1)}%</span>
                </div>
            </div>
        `).join('');
    }

    // ======================
    // 工具函数
    // ======================
    function getElementWithCheck(id) {
        const el = document.getElementById(id);
        if (!el) console.warn(`元素 #${id} 未找到`);
        return el;
    }

    function calculateSoftmax(logits) {
        const exp = logits.map(x => Math.exp(x));
        const sum = exp.reduce((a, b) => a + b);
        return exp.map(x => x / sum);
    }

    function validateDataStructure(data) {
        const required = ['text', 'image', 'result'];
        const resultRequired = ['logits_real_fake', 'logits_multicls'];
        return required.every(k => data.hasOwnProperty(k)) &&
               resultRequired.every(k => data.result.hasOwnProperty(k));
    }

    function getChartConfig(softmaxData) {
        return {
            type: 'doughnut',
            data: {
                labels: ['真实概率', '伪造概率'],
                datasets: [{
                    data: softmaxData,
                    backgroundColor: [CSS_VARS.success, CSS_VARS.danger],
                    borderWidth: 0
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                cutout: '70%',
                plugins: {
                    legend: { position: 'bottom' },
                    tooltip: { enabled: false }
                }
            }
        };
    }

    // ======================
    // 错误处理
    // ======================
    function handleAnalysisError(error) {
        console.error('分析错误:', error);
        elements.loadingOverlay.innerHTML = `
            <div class="error-message">
                <i class="fas fa-exclamation-triangle"></i>
                <h3>${error.message}</h3>
                <p>2秒后返回检测页面</p>
            </div>
        `;
        setTimeout(() => window.location.href = 'main.html', 2000);
    }
});

// ======================
// 公共接口（供其他脚本调用）
// ======================
window.AnalysisModule = {
    reloadWithNewData: (data) => {
        sessionStorage.setItem('analysisData', JSON.stringify(data));
        window.location.reload();
    }
};