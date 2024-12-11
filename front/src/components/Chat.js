    import React, { useState, useEffect, useRef } from 'react';
    import axios from 'axios';
    import io from 'socket.io-client'; // socket.io para tempo real
    import './Chat.css'; // o css para o estilo da página de chat

const Chat = () => {
    const [messages, setMessages] = useState([
        { username: 'User1', text: 'Olá, tudo bem?', timestamp: new Date().toISOString() },
        { username: 'User2', text: 'Sim, tudo certo!', timestamp: new Date().toISOString() },
    ]);
    const [message, setMessage] = useState('');
    const [username, setUsername] = useState('');

    const handleSendMessage = () => {
        if (!message || !username) return;

        const newMessage = {
            username,
            text: message,
            timestamp: new Date().toISOString(),
        };

        setMessages((prevMessages) => [...prevMessages, newMessage]);
        setMessage('');
    };

    return (
        <div className="chat-container">
            <div className="message-list">
                {messages.map((msg, index) => (
                    <div key={index} className="message-item">
                        <strong>{msg.username}</strong> <span>({new Date(msg.timestamp).toLocaleString()}):</span>
                        <p>{msg.text}</p>
                    </div>
                ))}
            </div>
            <div className="input-section">
                <input
                    type="text"
                    placeholder="Seu nome"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />
                <input
                    type="text"
                    placeholder="Digite sua mensagem"
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    onKeyDown={(e) => e.key === 'Enter' && handleSendMessage()}
                />
                <button onClick={handleSendMessage}>Enviar</button>
            </div>
        </div>
    );
};

export default Chat;
