let stompClient = null;

function connect() {
    const socket = new SockJS('/editor');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/line1', async (message) => {
            const pointsFromServer = JSON.parse(message.body);
            for (let i = 0; i < pointsFromServer.length; i++) {
                const point = pointsFromServer[i];
                drawPoint(point.x, point.y, "black");
                if (debugModeCheckbox.checked) {
                    await sleep(5);
                }
                console.log(`Точка ${i + 1}: x=${point.x}, y=${point.y}`);
            }
            console.log(pointsFromServer);
            console.log('Получены точки с WebSocket');
        });
        stompClient.subscribe('/topic/line2', async (message) => {
            const pointsFromServer = JSON.parse(message.body);
            for (let i = 0; i < pointsFromServer.length; i++) {
                const point = pointsFromServer[i];
                drawPointWU(point.x,point.y,point.intensity);
                if (debugModeCheckbox.checked) {
                    await sleep(5);
                }
                console.log(`Точка ${i + 1}: x=${point.x}, y=${point.y}`);
            }
            console.log(pointsFromServer);
            console.log('Получены точки с WebSocket');
        });
        stompClient.subscribe('/topic/line5', async (message) => {
            const pointsFromServer = JSON.parse(message.body);
            for (let i = 1; i < pointsFromServer.length; i++) {
                const pointStart = pointsFromServer[i-1];
                const pointEnd = pointsFromServer[i];
                drawPointCurve(pointStart.x,pointStart.y,pointEnd.x,pointEnd.y);
                if (debugModeCheckbox.checked) {
                    await sleep(5);
                }
                console.log(`Точка ${i + 1}: x=${pointStart.x}, y=${pointStart.y}   x=${pointEnd.x}, y=${pointEnd.y}`);
            }
            console.log(pointsFromServer);
            console.log('Получены точки с WebSocket');
        });
        stompClient.subscribe('/topic/line7', async (message) => {
            const pointsFromServer = JSON.parse(message.body);
            console.log(pointsFromServer);
            isConvex(pointsFromServer);
            console.log('Получены точки с WebSocket');
        });
        stompClient.subscribe('/topic/line6', async (message) => {
            const pointsFromServer = JSON.parse(message.body);
            console.log(pointsFromServer);
            clearPointsArray();
            for (let i = 0; i < pointsFromServer.length; i++) {
                const point = pointsFromServer[i];
                addPoints(point.x,point.y);
                console.log(`Точка ${i + 1}: x=${point.x}, y=${point.y}`);
            }
            redrawPolygon();
            console.log('Получены точки с WebSocket');
        });
        stompClient.subscribe('/topic/line8', async (message) => {
            const isInPolygon = JSON.parse(message.body);
            console.log(isInPolygon);
            polygonChecker(isInPolygon);
            console.log('Получены точки с WebSocket');
        });
        stompClient.subscribe('/topic/line9', async (message) => {
            const pointsFromServer = JSON.parse(message.body);
            console.log(pointsFromServer);
            drawNormals(pointsFromServer);
            console.log('Получены точки с WebSocket');
        });
        stompClient.subscribe('/topic/line10', async (message) => {
            const pointsFromServer = JSON.parse(message.body);
            for (let i = 0; i < pointsFromServer.length; i++) {
                const point = pointsFromServer[i];
                drawPoint5(point.x, point.y, "black");
                console.log(`Точка ${i + 1}: x=${point.x}, y=${point.y}`);
            }
            console.log(pointsFromServer);
            console.log('Получены точки с WebSocket');
        });
        stompClient.subscribe('/topic/line11', async (message) => {
            const pointsFromServer = JSON.parse(message.body);
            for (let i = 0; i < pointsFromServer.length; i++) {
                const point = pointsFromServer[i];
                drawPointWU5(point.x,point.y,point.intensity);
                console.log(`Точка ${i + 1}: x=${point.x}, y=${point.y}`);
            }
            console.log(pointsFromServer);
            console.log('Получены точки с WebSocket');
        });
        stompClient.subscribe('/topic/line12', async (message) => {
            const pointsFromServer = JSON.parse(message.body);
            await new Promise(resolve => setTimeout(resolve, 50));
            for (let i = 0; i < pointsFromServer.length; i++) {
                const point = pointsFromServer[i];
                drawPointScale(point.x, point.y, 'yellow');
                console.log(`Точка ${i + 1}: x=${point.x}, y=${point.y}`);
            }

            console.log('Все точки успешно отрисованы');
        });
        stompClient.subscribe('/topic/line13', async (message) => {
            const pointsFromServer = JSON.parse(message.body);
            await new Promise(resolve => setTimeout(resolve, 50));
            for (let i = 0; i < pointsFromServer.length; i++) {
                const point = pointsFromServer[i];
                drawPointUnscale(point.x, point.y);
                console.log(`Точка ${i + 1}: x=${point.x}, y=${point.y}`);
            }
            changeAlgorithmFill();
            console.log('Все точки успешно отрисованы');
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
}

window.addEventListener('load', () => {
    connect();
});


window.addEventListener('beforeunload', () => {
    disconnect();
});
