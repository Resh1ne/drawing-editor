const canvas = document.getElementById('canvasPolygon');
const ctx = canvas.getContext('2d');
const bresenhamButton = document.getElementById('Bresenham');
const ddaButton = document.getElementById('DDA');
const wuButton = document.getElementById('WU');
const bresenhamCircle = document.getElementById('BresenhamCircle');
const bresenhamEllipse = document.getElementById('BresenhamEllipse');
const bresenhamHyperbola = document.getElementById('BresenhamHyperbola');
const bresenhamParabola = document.getElementById('BresenhamParabola');
const triangulation = document.getElementById('triangulation');
const diagram = document.getElementById('voronoy');
const clearButton = document.getElementById('clear');

const debugModeCheckbox = document.getElementById('debugMode');

const submitButtonParabola = document.getElementById('submitButtonParabola');
const closeButton = document.querySelector('.close');
const parabolaModal = document.getElementById('parabolaModal');
const parameterParabola = document.getElementById('parameterParabola');
const submitButtonBezier = document.getElementById('submitButtonBezier');
const bezierModal = document.getElementById('bezierModal');
const parameterBezier = document.getElementById('parameterBezier');
const submitButtonBspline = document.getElementById('submitButtonBspline');
const bsplineModal = document.getElementById('bsplineModal');
const bsplineDegree = document.getElementById('parameterBspline2');
const bsplinePoints = document.getElementById('parameterBspline1');

let arrayLength = 2;
let points = [];
let lineAlgorithm = 'CDA';
let parabolaParam = 0;
let bezierPoints = 2;
let hermitePoints = 4;
let bsplineDegrees = 2;
let bsplineNumPoints = 3;


canvas.addEventListener('click', (event) => {
    console.log("CLICK");
    const rect = canvas.getBoundingClientRect();
    const x = Math.round(event.clientX - rect.left);
    const y = Math.round(event.clientY - rect.top);
    addPointInList(x, y);
});

function drawPoint(x, y, color) {
    let pointSize = 1;
    if (lineAlgorithm === 'points') {
        pointSize = 3;
    }
    ctx.beginPath();
    ctx.arc(x, y, pointSize, 0, 2 * Math.PI);
    ctx.fillStyle = color;
    ctx.fill();
    ctx.closePath();
}

function drawPointWU(x, y, alpha) {
    ctx.beginPath();
    ctx.arc(x, y, 1, 0, 2 * Math.PI);
    ctx.fillStyle = 'rgba(0, 0, 0, ' + alpha + ')';
    ctx.fill();
    ctx.closePath();
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function addPointInList(x, y) {
    if (points.length < arrayLength) {
        points.push({x: x, y: y});
        drawPoint(x, y, 'red');
    }
    if (lineAlgorithm === "CDA"
        || lineAlgorithm === 'Bresenham'
        || lineAlgorithm === 'WU'
        || lineAlgorithm === "BresenhamCircle"
        || lineAlgorithm === "BresenhamEllipse"
        || lineAlgorithm === "BresenhamHyperbola") {
        arrayLength = 2;
    } else if (lineAlgorithm === "BresenhamParabola") {
        arrayLength = 1;
        console.log("param применился");
    } else if (lineAlgorithm === "BezierCurve") {
        arrayLength = bezierPoints;
        console.log("bezier numbers применился")
    } else if ((lineAlgorithm === "HermiteCurve")) {
        arrayLength = hermitePoints;
        console.log("hermite points применился")
    } else if (lineAlgorithm === "BSplineCurve") {
        arrayLength = bsplineNumPoints;
    }
    if (points.length === arrayLength) {
        console.log("Вошло в 1 иф");
        if (lineAlgorithm === "CDA" || lineAlgorithm === 'Bresenham' || lineAlgorithm === 'WU') {
            let dataToSend = JSON.stringify({points, lineAlgorithm});
            if (stompClient && stompClient.connected) {
                stompClient.send('/app/lab1', {}, dataToSend);
            }
            points = [];
        } else if (lineAlgorithm === "BresenhamCircle" || lineAlgorithm === "BresenhamEllipse" || lineAlgorithm === "BresenhamHyperbola") {
            console.log("Вошло во 2 иф");
            let dataToSend = JSON.stringify({points, algorithm: lineAlgorithm});
            if (stompClient && stompClient.connected) {
                stompClient.send('/app/lab2', {}, dataToSend);
            }

            points = [];
        } else if (lineAlgorithm === "BresenhamParabola") {
            console.log("Вошло во 2 иф");
            let dataToSend = JSON.stringify({points, algorithm: lineAlgorithm, parabolaPar: parabolaParam});
            if (stompClient && stompClient.connected) {
                stompClient.send('/app/lab2', {}, dataToSend);
            }
            points = [];
        } else if (lineAlgorithm === "BezierCurve") {
            console.log("Вошло во 2 иф");
            let dataToSend = JSON.stringify({points, algorithm: lineAlgorithm});
            if (stompClient && stompClient.connected) {
                stompClient.send('/app/lab3', {}, dataToSend);
            }
            points = [];
        } else if (lineAlgorithm === "HermiteCurve") {
            console.log("Вошло во 2 иф");
            let dataToSend = JSON.stringify({points, algorithm: lineAlgorithm});
            if (stompClient && stompClient.connected) {
                stompClient.send('/app/lab3', {}, dataToSend);
            }
            points = [];
        } else if (lineAlgorithm === "BSplineCurve") {
            let dataToSend = JSON.stringify({points, algorithm: lineAlgorithm, degree: bsplineDegrees});
            if (stompClient && stompClient.connected) {
                stompClient.send('/app/lab3', {}, dataToSend);
            }
            points = [];
        }
    }
}

bresenhamCircle.addEventListener('click', () => {
    console.log('BresenhamCircle');
    lineAlgorithm = 'BresenhamCircle';
    points = [];
});

bresenhamEllipse.addEventListener('click', () => {
    console.log('BresenhamEllipse');
    lineAlgorithm = 'BresenhamEllipse';
    points = [];
});

bresenhamHyperbola.addEventListener('click', () => {
    console.log('BresenhamHyperbola');
    lineAlgorithm = 'BresenhamHyperbola';
    points = [];
});

bresenhamParabola.addEventListener('click', () => {
    console.log('bresenhamParabola');
    parabolaModal.style.display = 'block';
    lineAlgorithm = 'BresenhamParabola';
    points = [];
});

triangulation.addEventListener('click', () => {
   console.log('triangulation');
   if (lineAlgorithm === 'points') {
       let dataToSend = JSON.stringify({points, algorithm: 'triangulation'});
       if (stompClient && stompClient.connected) {
           stompClient.send('/app/lab7', {}, dataToSend);
       }
       points = [];
       arrayLength = 2;
       lineAlgorithm = 'CDA';
   } else {
       lineAlgorithm = 'points';
       arrayLength = 1000;
       points = [];
   }
});

diagram.addEventListener('click', () => {
    console.log('voronoy');
    if (lineAlgorithm === 'points') {
        let dataToSend = JSON.stringify({points, algorithm: 'voronoy'});
        if (stompClient && stompClient.connected) {
            stompClient.send('/app/lab7', {}, dataToSend);
        }
        points = [];
        lineAlgorithm = 'CDA';
        arrayLength = 2;
    } else {
        lineAlgorithm = 'points';
        arrayLength = 1000;
        points = [];
    }
});

bresenhamButton.addEventListener('click', () => {
    console.log('Выбран алгоритм Брезенхема');
    lineAlgorithm = 'Bresenham';
    points = [];
});

ddaButton.addEventListener('click', () => {
    console.log('Выбран алгоритм ЦДА');
    lineAlgorithm = 'CDA';
    points = [];
});

wuButton.addEventListener('click', () => {
    console.log('Выбран алгоритм ВУ');
    lineAlgorithm = 'WU';
    points = [];
});

clearButton.addEventListener('click', () => {
    console.log('Очистка холста');
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    points = [];
});

window.addEventListener('click', (event) => {
    if (event.target === parabolaModal) {
        parabolaModal.style.display = 'none';
    }
    if (event.target === bezierModal) {
        bezierModal.style.display = 'none';
    }
    if (event.target === bsplineModal) {
        bsplineModal.style.display = 'none';
    }
});

closeButton.addEventListener('click', () => {
    parabolaModal.style.display = 'none';
    bezierModal.style.display = 'none';
    bsplineModal.style.display = 'none';
});

submitButtonParabola.addEventListener('click', () => {
    const value = parameterParabola.value;
    if (value && !isNaN(value) && Number.isInteger(parseFloat(value))) {
        parabolaParam = parseInt(value, 10);
        console.log('Введённое значение:', parabolaParam);
        parabolaModal.style.display = 'none';
        parameterParabola.value = '';
    } else {
        alert('Пожалуйста, введите целое число.');
    }
});

