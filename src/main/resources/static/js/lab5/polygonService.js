// Глобальные переменные
const canvas = document.getElementById('canvasPolygon');
const clearButton = document.getElementById('clear');
const grahamButton = document.getElementById('grahamPolygon');
const jarvisButton = document.getElementById('jarvisPolygon');
const convexButton = document.getElementById('convex');
const pointButton = document.getElementById('point');
const normalButton = document.getElementById('normal');
const ddaButton = document.getElementById('DDA');
const bresenhamButton = document.getElementById('bresenham');
const wuButton = document.getElementById('wu');
const ctx = canvas.getContext('2d');
let points = [];
let linePoints = [];
let pointDefinition = false;
let algorithmLines = 'none';

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

    if (algorithmLines === 'none') {
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
    } else {
        if (algorithmLines === 'DDA') {

        } else if (algorithmLines === 'bresenham') {

        } else if (algorithmLines === 'wu') {

        }
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

function handleNormalButtonClick() {
    console.log("SEND NORMALS")
    let dataToSend = JSON.stringify({points, algorithm: "normalHandler"});
    if (stompClient && stompClient.connected) {
        stompClient.send('/app/lab5', {}, dataToSend);
    }
}

function handlePointButtonClick() {
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

function drawNormals(normals) {
    if (points.length < 2 || !normals || normals.length === 0) return;

    ctx.strokeStyle = 'green';
    ctx.lineWidth = 1;

    for (let i = 0; i < points.length; i++) {
        const nextIdx = (i + 1) % points.length;
        const p1 = points[i];
        const p2 = points[nextIdx];

        const midPoint = {
            x: (p1.x + p2.x) / 2,
            y: (p1.y + p2.y) / 2
        };

        const normal = normals[i % normals.length];

        const scale = 1;
        const normalEnd = {
            x: midPoint.x + normal.x * scale,
            y: midPoint.y + normal.y * scale
        };

        // Рисуем нормаль
        ctx.beginPath();
        ctx.moveTo(midPoint.x, midPoint.y);
        ctx.lineTo(normalEnd.x, normalEnd.y);

        // Рисуем стрелочку
        drawArrowhead(midPoint, normalEnd);

        ctx.stroke();
    }
}

function handleDDAButtonClick() {
    if (algorithmLines === 'DDA') {
        algorithmLines = 'none';
    } else {
        algorithmLines = 'DDA';
    }
}

function handleBresenhamButtonClick() {
    if (algorithmLines === 'bresenham') {
        algorithmLines = 'none';
    } else {
        algorithmLines = 'bresenham';
    }
}

function handleWuButtonClick() {
    if (algorithmLines === 'wu') {
        algorithmLines = 'none';
    } else {
        algorithmLines = 'wu';
    }
}

function drawArrowhead(from, to) {
    const headLength = 10;
    const angle = Math.atan2(to.y - from.y, to.x - from.x);

    ctx.moveTo(to.x, to.y);
    ctx.lineTo(
        to.x - headLength * Math.cos(angle - Math.PI / 6),
        to.y - headLength * Math.sin(angle - Math.PI / 6)
    );

    ctx.moveTo(to.x, to.y);
    ctx.lineTo(
        to.x - headLength * Math.cos(angle + Math.PI / 6),
        to.y - headLength * Math.sin(angle + Math.PI / 6)
    );
}

// Назначение обработчиков событий
function setupEventListeners() {
    document.addEventListener('DOMContentLoaded', function() {
        convexButton.addEventListener('click', handleConvexClick);
        grahamButton.addEventListener('click', handleGrahamClick);
        jarvisButton.addEventListener('click', handleJarvisClick);
        clearButton.addEventListener('click', handleClearClick);
        pointButton.addEventListener('click', handlePointButtonClick);
        normalButton.addEventListener('click', handleNormalButtonClick);
        ddaButton.addEventListener('click', handleDDAButtonClick);
        bresenhamButton.addEventListener('click', handleBresenhamButtonClick);
        wuButton.addEventListener('click', handleWuButtonClick);
        canvas.addEventListener('click', handleCanvasClick);
        document.addEventListener('keydown', handleKeyDown);
        initializeCanvas();
    });
}

// Запуск приложения
setupEventListeners();