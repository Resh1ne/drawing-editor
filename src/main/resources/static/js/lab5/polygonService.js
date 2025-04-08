// Глобальные переменные
const canvas = document.getElementById('canvasPolygon');
const clearButton = document.getElementById('clear');
const grahamButton = document.getElementById('grahamPolygon');
const jarvisButton = document.getElementById('jarvisPolygon');
const convexButton = document.getElementById('convex');
const pointButton = document.getElementById('point');
const ctx = canvas.getContext('2d');
let points = [];
let pointDefinition = false;

// Функции работы с canvas
function clearCanvas() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
}

function drawPoint(x, y) {
    ctx.beginPath();
    ctx.arc(x, y, 5, 0, Math.PI * 2);
    ctx.fillStyle = 'red';
    ctx.fill();
    ctx.closePath();
}

function drawLines() {
    if (points.length < 2) return;

    ctx.beginPath();
    ctx.moveTo(points[0].x, points[0].y);

    for (let i = 1; i < points.length; i++) {
        ctx.lineTo(points[i].x, points[i].y);
    }

    if (points.length > 2) {
        ctx.closePath();
        ctx.fillStyle = 'rgba(100, 200, 255, 0.3)';
        ctx.fill();
    }

    ctx.strokeStyle = 'blue';
    ctx.lineWidth = 2;
    ctx.stroke();
    ctx.closePath();
}

function clearPointsArray() {
    points = [];
}

function addPoints(x,y) {
    points.push({x,y});
}

function handleConvexClick() {
    let dataToSend = JSON.stringify({points, algorithm: "isConvex"});
    if (stompClient && stompClient.connected) {
        stompClient.send('/app/lab5', {}, dataToSend);
    }
}

function handleGrahamClick() {
    let dataToSend = JSON.stringify({points, algorithm: "GrahamBuilder"});
    if (stompClient && stompClient.connected) {
        stompClient.send('/app/lab5', {}, dataToSend);
    }
}

function handleJarvisClick() {
    let dataToSend = JSON.stringify({points, algorithm: "JarvisBuilder"});
    if (stompClient && stompClient.connected) {
        stompClient.send('/app/lab5', {}, dataToSend);
    }
}

function handleClearClick() {
    points = [];
    clearCanvas();
}

function redrawPolygon() {
    clearCanvas();
    points.forEach(point => drawPoint(point.x, point.y));
    drawLines();
}

function handleCanvasClick(e) {
    const rect = canvas.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;

    if (pointDefinition) {
        let dataToSend = JSON.stringify({points, algorithm: "PointDefinition", x: x, y: y});
        if (stompClient && stompClient.connected) {
            stompClient.send('/app/lab5', {}, dataToSend);
        }
    } else {
        points.push({ x, y });
        clearCanvas();
        points.forEach(point => drawPoint(point.x, point.y));
        drawLines();
    }
}

function handleKeyDown(e) {
    if (e.key === 'Escape') {
        points = [];
        clearCanvas();
    }
}

function isConvex(value) {
    if(value === true) {
        alert("Полигон выпуклый");
    } else {
        alert("Полигон вогнутый");
    }
}

function polygonChecker(value) {
    if(value === true) {
        alert("Точка внутри полигона");
    } else {
        alert("Точка снаружи полигона");
    }
}

function handlePointBuuttonClick() {
    if (pointDefinition) {
        pointDefinition = false;
    } else {
        pointDefinition = true;
    }
}

// Инициализация при загрузке страницы
function initializeCanvas() {
    clearCanvas();
    ctx.strokeStyle = 'blue';
    ctx.lineWidth = 2;
    ctx.fillStyle = 'rgba(100, 200, 255, 0.3)';
}

// Назначение обработчиков событий
function setupEventListeners() {
    document.addEventListener('DOMContentLoaded', function() {
        convexButton.addEventListener('click', handleConvexClick);
        grahamButton.addEventListener('click', handleGrahamClick);
        jarvisButton.addEventListener('click', handleJarvisClick);
        clearButton.addEventListener('click', handleClearClick);
        pointButton.addEventListener('click', handlePointBuuttonClick);
        canvas.addEventListener('click', handleCanvasClick);
        document.addEventListener('keydown', handleKeyDown);
        initializeCanvas();
    });
}

// Запуск приложения
setupEventListeners();