let stompClient = null;

function connect() {
    const socket = new SockJS('/editor');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/line', (message) => {
            const pointsFromServer = JSON.parse(message.body);
            checkDebugMode(pointsFromServer);
            console.log('Received points from server');
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



///////////////////////////////// для тестовой отправки данных с фронта в json.


const canvas = document.getElementById('canvasPolygon');
const ctx = canvas.getContext('2d');


canvas.addEventListener('click', (event) => {
    console.log("CLICK");
    const rect = canvas.getBoundingClientRect();
    const x = event.clientX - rect.left;
    const y = event.clientY - rect.top;
    let dataToSend = JSON.stringify({x: x, y: y})
    if (stompClient && stompClient.connected) {
        stompClient.send('/app/lab1', {}, dataToSend);
    }

});