// client.js
const WebSocket = require('ws');

const socket = new WebSocket("ws://localhost:8080/ctx/ws");

socket.on('open', () => {
    console.log("Connected to server!");
    socket.send("Hello from Node!");
});

socket.on('message', (data) => {
    console.log("Received:", data);
});

socket.on('error', (err) => {
    console.error("WebSocket error:", err);
});