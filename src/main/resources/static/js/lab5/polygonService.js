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
const polygonButton = document.getElementById('polygon');
const fill1 = document.getElementById('fill1');
const fill2 = document.getElementById('fill2');
const fill3 = document.getElementById('fill3');
const fill4 = document.getElementById('fill4');
const debugModeCheckbox = document.getElementById('debugMode');
const ctx = canvas.getContext('2d');
let points = [];
let linePoints = [];
let pointDefinition = false;
let algorithmLines = 'none';
let algorithmFill = 'none';

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

function addPoints(x, y) {
    points.push({x, y});
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

function drawPoint5(x, y, color) {
    ctx.beginPath();
    ctx.arc(x, y, 1, 0, 2 * Math.PI);
    ctx.fillStyle = color;
    ctx.fill();
    ctx.closePath();
}

function drawPointWU5(x, y, alpha) {
    ctx.beginPath();
    ctx.arc(x, y, 1, 0, 2 * Math.PI);
    ctx.fillStyle = 'rgba(0, 0, 0, ' + alpha + ')';
    ctx.fill();
    ctx.closePath();
}

function handleCanvasClick(e) {
    const rect = canvas.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const y = e.clientY - rect.top;

    if (algorithmFill !== 'none') {
        chooseCorrectFillMethod(x,y);
    } else {
        if (algorithmLines === 'none') {
            if (pointDefinition) {
                let dataToSend = JSON.stringify({points, algorithm: "PointDefinition", x: x, y: y});
                if (stompClient && stompClient.connected) {
                    stompClient.send('/app/lab5', {}, dataToSend);
                }
            } else {
                points.push({x, y});
                clearCanvas();
                points.forEach(point => drawPoint(point.x, point.y));
                drawLines();
            }
        } else {
            linePoints.push({x, y});
            if (algorithmLines === 'DDA') {
                if (linePoints.length === 2) {
                    let dataToSend = JSON.stringify({points: linePoints, algorithm: "CDA"});
                    if (stompClient && stompClient.connected) {
                        stompClient.send('/app/lab5', {}, dataToSend);
                    }
                    dataToSend = JSON.stringify({points, linePoints, algorithm: "Intersection"});
                    if (stompClient && stompClient.connected) {
                        stompClient.send('/app/lab5', {}, dataToSend);
                    }
                    linePoints = [];
                }
            } else if (algorithmLines === 'bresenham') {
                if (linePoints.length === 2) {
                    let dataToSend = JSON.stringify({points: linePoints, algorithm: "Bresenham"});
                    if (stompClient && stompClient.connected) {
                        stompClient.send('/app/lab5', {}, dataToSend);
                    }
                    dataToSend = JSON.stringify({points, linePoints, algorithm: "Intersection"});
                    if (stompClient && stompClient.connected) {
                        stompClient.send('/app/lab5', {}, dataToSend);
                    }
                    linePoints = [];
                }
            } else if (algorithmLines === 'wu') {
                if (linePoints.length === 2) {
                    let dataToSend = JSON.stringify({points: linePoints, algorithm: "WU"});
                    if (stompClient && stompClient.connected) {
                        stompClient.send('/app/lab5', {}, dataToSend);
                    }
                    dataToSend = JSON.stringify({points, linePoints, algorithm: "Intersection"});
                    if (stompClient && stompClient.connected) {
                        stompClient.send('/app/lab5', {}, dataToSend);
                    }
                    linePoints = [];
                }
            }
        }
    }
}

function drawPointScale(x, y, color) {
    ctx.beginPath();
    ctx.arc(x, y, 5, 0, 2 * Math.PI);
    ctx.fillStyle = color;
    ctx.fill();
    ctx.closePath();
}

function drawPointUnscale(x, y) {
    console.log(algorithmFill);
    color = 'green';
    if (algorithmFill === 'fill1') {
        color = 'yellow';
    } else if (algorithmFill === 'fill2') {
        color = 'blue';
    } else if (algorithmFill === 'fill3') {
        color = 'red';
    }

    // Если включён дебаг-режим, рисуем с задержкой
    if (debugModeCheckbox.checked) {
        setTimeout(() => {
            drawPoint6(x, y, color);
        }, 10); // Задержка 10 мс (можно регулировать)
    } else {
        drawPoint6(x, y, color); // Без задержки
    }
}

function changeAlgorithmFill() {
    algorithmFill = 'none';
}

// Вынес отрисовку точки в отдельную функцию для удобства
function drawPoint6(x, y, color) {
    ctx.beginPath();
    ctx.arc(x, y, 1, 0, 2 * Math.PI);
    ctx.fillStyle = color;
    ctx.fill();
    ctx.closePath();
}

function drawBeautifulPoint(point, size = 8) {
    console.log("POINT")
    ctx.save();

    // Внешний круг
    ctx.beginPath();
    ctx.arc(point.x, point.y, size + 2, 0, Math.PI * 2);
    ctx.fillStyle = 'rgba(0, 80, 0, 0.5)';
    ctx.fill();

    // Основная точка
    ctx.beginPath();
    ctx.arc(point.x, point.y, size, 0, Math.PI * 2);
    ctx.fillStyle = '#00cc00';
    ctx.fill();

    // Контур
    ctx.beginPath();
    ctx.arc(point.x, point.y, size, 0, Math.PI * 2);
    ctx.strokeStyle = '#ffffff';
    ctx.lineWidth = 1;
    ctx.stroke();

    ctx.restore();
}

function handleKeyDown(e) {
    if (e.key === 'Escape') {
        points = [];
        clearCanvas();
    }
}

function isConvex(value) {
    if (value === true) {
        alert("Полигон выпуклый");
    } else {
        alert("Полигон вогнутый");
    }
}

function polygonChecker(value) {
    if (value === true) {
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

function handlePolygonButtonClick() {
    algorithmLines = 'none';
}

function chooseCorrectFillMethod(x,y) {
    let value = 'fill1';
    switch (algorithmFill) {
        case 'fill1': value = 'fill1'; break;
        case 'fill2': value = 'fill2'; break;
        case 'fill3': value = 'fill3'; break;
        case 'fill4': value = 'fill4'; break;
    }
    let dataToSend = JSON.stringify({points, x, y, algorithm: value});
    if (stompClient && stompClient.connected) {
        stompClient.send('/app/lab6', {}, dataToSend);
    }
}

function handleFill1Click() {
    if (algorithmFill === 'fill1') {
        algorithmFill = 'none';
    } else {
        algorithmFill = 'fill1';
    }
}

function handleFill2Click() {
    if (algorithmFill === 'fill2') {
        algorithmFill = 'none';
    } else {
        algorithmFill = 'fill2';
    }
}

function handleFill3Click() {
    if (algorithmFill === 'fill3') {
        algorithmFill = 'none';
    } else {
        algorithmFill = 'fill3';
    }
}

function handleFill4Click() {
    if (algorithmFill === 'fill4') {
        algorithmFill = 'none';
    } else {
        algorithmFill = 'fill4';
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
    document.addEventListener('DOMContentLoaded', function () {
        convexButton.addEventListener('click', handleConvexClick);
        grahamButton.addEventListener('click', handleGrahamClick);
        jarvisButton.addEventListener('click', handleJarvisClick);
        clearButton.addEventListener('click', handleClearClick);
        pointButton.addEventListener('click', handlePointButtonClick);
        normalButton.addEventListener('click', handleNormalButtonClick);
        ddaButton.addEventListener('click', handleDDAButtonClick);
        bresenhamButton.addEventListener('click', handleBresenhamButtonClick);
        wuButton.addEventListener('click', handleWuButtonClick);
        polygonButton.addEventListener('click', handlePolygonButtonClick);
        canvas.addEventListener('click', handleCanvasClick);
        fill1.addEventListener('click', handleFill1Click);
        fill2.addEventListener('click', handleFill2Click);
        fill3.addEventListener('click', handleFill3Click);
        fill4.addEventListener('click', handleFill4Click);
        document.addEventListener('keydown', handleKeyDown);
        initializeCanvas();
    });
}

// Запуск приложения
setupEventListeners();