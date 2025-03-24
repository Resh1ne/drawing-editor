const bezier = document.getElementById('Bezier')
const hermite = document.getElementById('Hermite')
const bspline = document.getElementById('Bspline')

function drawPointCurve(xs, ys, xe, ye) {
    ctx.beginPath();
    ctx.moveTo(xs, ys);
    ctx.lineTo(xe, ye);
    ctx.strokeStyle = 'black';
    ctx.lineWidth = 1;
    ctx.stroke();
}

hermite.addEventListener('click', () => {
    console.log("Hermite Curve");
    lineAlgorithm = "HermiteCurve";
    points = [];
});

bspline.addEventListener('click', () => {
    console.log("BsplineCurve");
    bsplineModal.style.display = 'block';
    lineAlgorithm = "BSplineCurve";
    points = [];
});


bezier.addEventListener('click', () => {
    console.log("Bezier Curve");
    bezierModal.style.display = 'block';
    lineAlgorithm = "BezierCurve";
    points = [];
});

submitButtonBezier.addEventListener('click', () => {
    const value = parameterBezier.value;
    if (value && !isNaN(value) && Number.isInteger(parseFloat(value))) {
        let checked = parseInt(value, 10);
        if (checked < 2) {
            alert('Пожалуйста, введите число больше либо равное 2.');
        } else {
            bezierPoints = parseInt(value, 10);
            console.log('Введённое значение:', bezierPoints);
            bezierModal.style.display = 'none';
            parameterBezier.value = '';
        }
    } else {
        alert('Пожалуйста, введите целое число.');
    }
});

submitButtonBspline.addEventListener('click', () => {
    const value1 = bsplineDegree.value;
    const value2 = bsplinePoints.value;
    if ((value1 && !isNaN(value1) && Number.isInteger(parseFloat(value1)))
        && (value2 && !isNaN(value2) && Number.isInteger(parseFloat(value2)))) {
        let checked1 = parseInt(value1, 10);
        let checked2 = parseInt(value2, 10);
        if (checked1 < 2) {
            alert('Пожалуйста, введите степень больше 2.');
        } else if (checked2 < 3) {
            alert('Пожалуйста, введите количество точек больше 3.');
        } else {
            bsplineDegrees = parseInt(value1, 10);
            bsplineNumPoints = parseInt(value2, 10);
            console.log('Введённое значение:', bsplineDegrees);
            console.log('Введённое значение:', bsplineNumPoints);
            bsplineModal.style.display = 'none';
            bsplineDegree.value = '';
        }
    } else {
        alert('Пожалуйста, введите целое число.');
    }
});