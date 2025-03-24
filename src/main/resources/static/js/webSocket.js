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
