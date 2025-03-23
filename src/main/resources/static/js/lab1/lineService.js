const canvas = document.getElementById('canvasPolygon');
const ctx = canvas.getContext('2d');
const bresenhamButton = document.getElementById('Bresenham');
const ddaButton = document.getElementById('DDA');
const wuButton = document.getElementById('WU');
const clearButton = document.getElementById('clear');

let points = [];
let lineAlgorithm = 'CDA';

canvas.addEventListener('click', (event) => {
    console.log("CLICK");
    const rect = canvas.getBoundingClientRect();
    const x = Math.round(event.clientX - rect.left);
    const y = Math.round(event.clientY - rect.top);
    addPointInList(x,y);
});

function drawPoint(x, y, color) {
    ctx.beginPath();
    ctx.arc(x, y, 1, 0, 2 * Math.PI);
    ctx.fillStyle = color;
    ctx.fill();
    ctx.closePath();
}

function drawPointWU(x, y, alpha) {
    ctx.beginPath();
    ctx.arc(x, y, 1, 0, 2 * Math.PI);
    ctx.fillStyle = 'rgba(0, 0, 255, ' + alpha + ')';
    ctx.fill();
    ctx.closePath();
}

function addPointInList(x,y) {
    if (points.length < 2) {
        points.push({x: x, y: y});
        drawPoint(x,y, 'red');
    }
    if (points.length === 2) {
        let dataToSend = JSON.stringify({points, lineAlgorithm});
        if (stompClient && stompClient.connected) {
            stompClient.send('/app/lab1', {}, dataToSend);
        }
        points = [];
    }
}

bresenhamButton.addEventListener('click', () => {
    console.log('Выбран алгоритм Брезенхема');
    lineAlgorithm = 'Bresenham';
});

ddaButton.addEventListener('click', () => {
    console.log('Выбран алгоритм ЦДА');
    lineAlgorithm = 'CDA';
});

wuButton.addEventListener('click', () => {
    console.log('Выбран алгоритм ВУ');
    lineAlgorithm = 'WU';
});

clearButton.addEventListener('click', () => {
    console.log('Очистка холста');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    points = [];
});

