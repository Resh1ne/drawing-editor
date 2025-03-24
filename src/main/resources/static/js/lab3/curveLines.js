const bezier = document.getElementById('Bezier')

function drawPointCurve(xs,ys,xe,ye) {
    ctx.beginPath();
    ctx.moveTo(xs, ys);
    ctx.lineTo(xe, ye);
    ctx.strokeStyle = 'black';
    ctx.lineWidth = 1;
    ctx.stroke();
}

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