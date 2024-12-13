import React, { useState, useEffect, useRef } from 'react';
import { FaPaperPlane } from 'react-icons/fa';
import axios from 'axios';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import './Chat.css';

const Chat = () => {
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState('');
    const [username, setUsername] = useState('');
    const [stompClient, setStompClient] = useState(null);

    // Referência para a lista de mensagens
    const messagesEndRef = useRef(null);

    useEffect(() => {
        const loggedInUser = localStorage.getItem('loggedInUser');
        if (loggedInUser) {
            const user = JSON.parse(loggedInUser);
            setUsername(user.username);
        }
    }, []);

    // Conectar ao WebSocket
    useEffect(() => {
        const socket = new SockJS('http://localhost:8080/chat-websocket');
        const client = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                console.log('Conectado ao WebSocket');
                client.subscribe('/topic/mensagens', (message) => {
                    // Atualiza a lista de mensagens e rola para a última
                    setMessages((prevMessages) => [...prevMessages, JSON.parse(message.body)]);
                });
            },
            onStompError: (error) => {
                console.error('Erro STOMP:', error);
            },
        });

        client.activate();
        setStompClient(client);

        return () => {
            client.deactivate();
        };
    }, []);

    // Função para enviar a mensagem
    const handleSendMessage = async () => {
        if (!message || !username) return;

        const newMessage = {
            conteudo: message,
            remetente: username,
        };

        try {
            // Envia a mensagem via POST
            await axios.post('http://localhost:8080/mensagens/enviar', newMessage);
            setMessage('');
        } catch (error) {
            console.error('Erro ao enviar mensagem:', error);
        }
    };

    // Função para carregar as mensagens
    const loadMessages = async () => {
        try {
            const response = await axios.get('http://localhost:8080/mensagens');
            setMessages(response.data);
        } catch (error) {
            console.error('Erro ao carregar mensagens:', error);
        }
    };

    useEffect(() => {
        loadMessages();
    }, []);

    // Efeito para rolar a página para a última mensagem sempre que as mensagens mudarem
    useEffect(() => {
        if (messagesEndRef.current) {
            messagesEndRef.current.scrollIntoView({ behavior: 'smooth' });
        }
    }, [messages]);

    return (
        <div className="chat-container">
            <div className="message-list">
                {messages.length > 0 ? (
                    messages.map((msg, index) => (
                        <div key={index} className="message-item">
                            <div className="message-body">
                                <strong style={{ color: '#7eabff' }}>{msg.remetente}</strong>{' '}
                                <span className="message-timestamp">
                                    {new Date(msg.dataEnvio).toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit' })} às{' '}
                                    {new Date(msg.dataEnvio).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                                </span>
                                <p>{msg.conteudo}</p>
                            </div>

                        </div>
                    ))
                ) : (
                    <div className="no-messages">Ainda não há mensagens.</div>
                )}
                {/* Este ref ajuda a rolar até a última mensagem */}
                <div ref={messagesEndRef} />
            </div>
            <div className="input-section">
                <input
                    type="text"
                    placeholder="Digite sua mensagem"
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    onKeyDown={(e) => e.key === 'Enter' && handleSendMessage()} // Permite envio ao pressionar Enter
                />
                <button onClick={handleSendMessage}>
                    <FaPaperPlane size={20} />
                </button>
            </div>
        </div>
    );
};

export default Chat;
