class WebSocketConnector {
    constructor(url) {
        this.url = url;
        this.socket = null;
        this.stompClient = null;
    }

    connect(subscriptions = []) {
        this.socket = new SockJS(this.url);
        this.stompClient = Stomp.over(this.socket);
        const self = this;
        this.stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            subscriptions.forEach(sub => {
                self.stompClient.subscribe(sub.topic, sub.callback);
            });
        }, function (error) {
            console.error('WebSocket ошибка:', error);
        });
    }

    send(destination, message) {
        if (this.stompClient) {
            this.stompClient.send(destination, {}, JSON.stringify(message));
        } else {
            console.error("STOMP клиент не подключен.");
        }
    }
}

const drawingEditor = {
    pixels: null,
    canvas: null,
    context: null,

    socketConnection: null,
    debug: false,
    activeTab: 'lines', center: null, linePoints: [], curvePoints: [], polygonPoints: [], voronoiPoints: [],
    current3DObject: null,
    transformationMatrix: null,

    init() {
        this.canvas = document.getElementById('canvas');
        this.context = this.canvas.getContext('2d');
        this.initWebSocket();
    },

    initWebSocket() {
        const subscriptions = [{
            topic: '/topic/drawings3d', callback: (message) => {
                const response = JSON.parse(message.body);
                console.log('Получены данные с сервера (3D):', response);
                this.clearCanvas();
                this.drawPixels(response.pixels);
                console.log('Текущая матрица преобразования:', response.matrix);
                this.transformationMatrix = response.matrix;
            }
        }];
        this.socketConnection = new WebSocketConnector('/editor');
        this.socketConnection.connect(subscriptions);
    },


    clearCanvas() {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
    },

    clear() {
        this.clearCanvas();
    },

    drawPixels(pixels) {
        if (Array.isArray(pixels)) {
            this.pixels = pixels;
            if (this.debug) {
                pixels.forEach((pixel, index) => {
                    setTimeout(() => {
                        this.context.fillStyle = pixel.color || 'black';
                        this.context.fillRect(pixel.x, pixel.y, 1, 1);
                    }, index * 10);
                });
            } else {
                pixels.forEach(pixel => {
                    this.context.fillStyle = pixel.color || 'black';
                    this.context.fillRect(pixel.x, pixel.y, 1, 1);
                });
            }
        } else if (pixels.vertices && pixels.edges) {
            if (this.debug) {
                pixels.edges.forEach((edge, index) => {
                    setTimeout(() => {
                        const startVertex = pixels.vertices[edge.start];
                        const endVertex = pixels.vertices[edge.end];
                        this.context.beginPath();
                        this.context.moveTo(startVertex.x, startVertex.y);
                        this.context.lineTo(endVertex.x, endVertex.y);
                        this.context.stroke();
                    }, index * 10);
                });
            } else {
                pixels.edges.forEach(edge => {
                    const startVertex = pixels.vertices[edge.start];
                    const endVertex = pixels.vertices[edge.end];
                    this.context.beginPath();
                    this.context.moveTo(startVertex.x, startVertex.y);
                    this.context.lineTo(endVertex.x, endVertex.y);
                    this.context.stroke();
                });
            }
        }
    },


    load3DObject() {
        const fileInput = document.getElementById('objFileInput');
        if (fileInput.files.length === 0) {
            alert("Выберите файл для загрузки!");
            return;
        }
        const file = fileInput.files[0];
        const reader = new FileReader();
        reader.onload = (event) => {
            const fileContent = event.target.result;
            const objectData = this.parseOBJFile(fileContent);
            this.current3DObject = objectData;
            alert(`Загружено вершин: ${objectData.vertices.length}`);
            const request = {
                transformationType: "none",
                vertices: this.transformVerticesForServer(objectData.vertices),
                edges: this.transformEdgesForServer(objectData.edges)
            };
            this.socketConnection.send("/app/transform3D", request);
        };
        reader.readAsText(file);
    },

    parseOBJFile(content) {
        const vertices = [];
        const edges = [];
        const lines = content.split("\n");
        lines.forEach(line => {
            if (line.startsWith("v ")) {
                const parts = line.split(" ").filter(p => p.trim() !== "");
                const x = parseFloat(parts[1]);
                const y = parseFloat(parts[2]);
                const z = parseFloat(parts[3]);
                vertices.push([x, y, z, 1]);
            } else if (line.startsWith("f ")) {
                const parts = line.split(" ").filter(p => p.trim() !== "");
                const faceIndices = parts.slice(1).map(part => parseInt(part.split('/')[0], 10) - 1);
                for (let i = 0; i < faceIndices.length; i++) {
                    const startIndex = faceIndices[i];
                    const endIndex = faceIndices[(i + 1) % faceIndices.length];
                    edges.push([startIndex, endIndex]);
                }
            }
        });
        return {vertices, edges};
    },

    transform3DControl(type, params) {
        if (!this.current3DObject) {
            alert("Сначала загрузите 3D объект!");
            return;
        }
        const request = {
            transformationType: type,
            vertices: this.current3DObject.vertices,
            edges: this.current3DObject.edges,
            matrix: this.transformationMatrix
        };
        Object.assign(request, params);
        this.socketConnection.send("/app/transform3D", request);
    },

};

document.addEventListener('DOMContentLoaded', function () {
    drawingEditor.init();
});

document.addEventListener('keydown', function (event) {
    const angle = 5;
    const scaleFactor = 1.1;
    const moveStep = 0.1;
    const perspectiveStep = 10;

    switch (event.code) {
        case 'ArrowUp':
        case 'ArrowDown':
        case 'ArrowLeft':
        case 'ArrowRight':
            event.preventDefault();
            break;
    }

    switch (event.code) {
        case 'NumpadAdd':
        case 'Equal':
            drawingEditor.transform3DControl('scaling', {sx: scaleFactor, sy: scaleFactor, sz: scaleFactor});
            break;
        case 'NumpadSubtract':
        case 'Minus':
            drawingEditor.transform3DControl('scaling', {
                sx: 1 / scaleFactor,
                sy: 1 / scaleFactor,
                sz: 1 / scaleFactor
            });
            break;
        case 'ArrowUp':
            drawingEditor.transform3DControl('rotationX', {angle: angle});
            break;
        case 'ArrowDown':
            drawingEditor.transform3DControl('rotationX', {angle: -angle});
            break;
        case 'ArrowLeft':
            drawingEditor.transform3DControl('rotationY', {angle: angle});
            break;
        case 'ArrowRight':
            drawingEditor.transform3DControl('rotationY', {angle: -angle});
            break;
        case 'KeyW':
            drawingEditor.transform3DControl('translation', {x: 0, y: moveStep, z: 0});
            break;
        case 'KeyS':
            drawingEditor.transform3DControl('translation', {x: 0, y: -moveStep, z: 0});
            break;
        case 'KeyA':
            drawingEditor.transform3DControl('translation', {x: -moveStep, y: 0, z: 0});
            break;
        case 'KeyD':
            drawingEditor.transform3DControl('translation', {x: moveStep, y: 0, z: 0});
            break;
        case 'KeyQ':
            drawingEditor.transform3DControl('translation', {x: 0, y: 0, z: moveStep});
            break;
        case 'KeyE':
            drawingEditor.transform3DControl('translation', {x: 0, y: 0, z: -moveStep});
            break;
        case 'KeyP':
            drawingEditor.transform3DControl('perspective', {d: perspectiveStep});
            break;
        case 'KeyO':
            drawingEditor.transform3DControl('perspective', {d: -perspectiveStep});
            break;
    }
});