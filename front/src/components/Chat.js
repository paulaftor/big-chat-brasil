import React, { useState, useEffect } from 'react';
import { FaPaperPlane } from 'react-icons/fa';
import './Chat.css';

const Chat = () => {
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState('');
    const [username, setUsername] = useState('');

    // Função para enviar a mensagem
    const handleSendMessage = async () => {
        if (!message || !username) return;

        const newMessage = {
            username,
            conteudo: message, // Ajuste o nome do campo se necessário
            timestamp: new Date().toISOString(),
        };

        try {
            const response = await fetch('/mensagens', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(newMessage),
            });

            if (response.ok) {
                const savedMessage = await response.json();
                setMessages((prevMessages) => [...prevMessages, savedMessage]);
                setMessage(''); // Limpa o campo de input
            } else {
                console.error('Erro ao salvar mensagem');
            }
        } catch (error) {
            console.error('Erro na requisição:', error);
        }
    };

    // Função para carregar mensagens do banco
    const loadMessages = async () => {
        try {
            const response = await fetch('/mensagens');
            if (response.ok) {
                const fetchedMessages = await response.json();
                setMessages(fetchedMessages);
            } else {
                console.error('Erro ao carregar mensagens');
            }
        } catch (error) {
            console.error('Erro na requisição:', error);
        }
    };

    // Carrega mensagens na inicialização
    useEffect(() => {
        loadMessages();
    }, []);

    return (
        <div className="chat-container">
            <div className="message-list">
                {messages.map((msg, index) => (
                    <div key={index} className="message-item">
                        <div className="message-body">
                            <strong style={{ color: '#7eabff' }}>{msg.username}</strong>{' '}
                            <span className="message-timestamp">
                                {new Date(msg.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                            </span>
                            <p>{msg.conteudo}</p>
                        </div>
                    </div>
                ))}
            </div>
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
