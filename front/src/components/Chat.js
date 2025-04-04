import React, { useState, useEffect, useRef } from 'react';
import { FaPaperPlane } from 'react-icons/fa';
import axios from 'axios';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import './Chat.css';

const Chat = () => {
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState('');
    const [telefoneDestinatario, setTelefoneDestinatario] = useState('');
    const [isWhatsapp, setIsWhatsapp] = useState(false);
    const [username, setUsername] = useState('');
    const [stompClient, setStompClient] = useState(null);
    const messagesEndRef = useRef(null);

    useEffect(() => {
        const loggedInUser = localStorage.getItem('loggedInUser');
        if (loggedInUser) {
            const user = JSON.parse(loggedInUser);
            setUsername(user.username);
        }
    }, []);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8080/chat-websocket');
        const client = new Client({
            webSocketFactory: () => socket,
            onConnect: () => {
                console.log('Conectado ao WebSocket');
                client.subscribe('/topic/mensagens', (message) => {
                    setMessages((prev) => [...prev, JSON.parse(message.body)]);
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

    const handleSendMessage = async () => {
        if (!message || !telefoneDestinatario) return;

        const newMessage = {
            conteudo: message,
            telefoneDestinatario,
            flWhatsapp: isWhatsapp,
        };

        try {
            await axios.post('http://localhost:8080/mensagens/enviar', newMessage);
            setMessage('');
        } catch (error) {
            console.error('Erro ao enviar mensagem:', error);
        }
    };

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
                                <strong style={{ color: '#7eabff' }}>{msg.remetente || 'Sistema'}</strong>{' '}
                                <span className="message-timestamp">
                                    {new Date(msg.dataEnvio).toLocaleDateString('pt-BR')} às{' '}
                                    {new Date(msg.dataEnvio).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                                </span>
                                <p>{msg.conteudo}</p>
                                <small>
                                    Para: {msg.telefoneDestinatario} •{' '}
                                    {msg.flWhatsapp ? 'WhatsApp' : 'SMS'} •{' '}
                                    Status: <strong>{msg.status}</strong>
                                </small>
                            </div>
                        </div>
                    ))
                ) : (
                    <div className="no-messages">Ainda não há mensagens.</div>
                )}
                <div ref={messagesEndRef} />
            </div>

            {/* Campos adicionais acima do campo de mensagem */}
            <div className="input-section extra-inputs">
                <input
                    type="text"
                    placeholder="Telefone do destinatário"
                    value={telefoneDestinatario}
                    onChange={(e) => setTelefoneDestinatario(e.target.value)}
                    required
                />
                <label className="whatsapp-flag">
                    <input
                        type="checkbox"
                        checked={isWhatsapp}
                        onChange={() => setIsWhatsapp((prev) => !prev)}
                    />
                    Enviar via WhatsApp
                </label>
            </div>

            {/* Campo de mensagem e botão de envio */}
            <div className="input-section">
                <input
                    type="text"
                    placeholder="Digite sua mensagem"
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    onKeyDown={(e) => e.key === 'Enter' && handleSendMessage()}
                />
                <button onClick={handleSendMessage}>
                    <FaPaperPlane size={20} />
                </button>
            </div>
        </div>
    );
};

export default Chat;
