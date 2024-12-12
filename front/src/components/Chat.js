import React, { useState, useEffect } from 'react';
import { FaPaperPlane } from 'react-icons/fa';
import './Chat.css';

const Chat = () => {
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState('');
    const [username, setUsername] = useState('');

    // Recuperar as informações do usuário logado do local storage na inicialização
    useEffect(() => {
        const loggedInUser = localStorage.getItem('loggedInUser');
        if (loggedInUser) {
            const user = JSON.parse(loggedInUser);
            setUsername(user.username);
        }
    }, []);

    // Função para enviar a mensagem
    const handleSendMessage = async () => {
        if (!message || !username) return;

        const newMessage = {
            username,
            conteudo: message,
            timestamp: new Date().toISOString(), // Timestamp no formato ISO 8601
        };

        try {
            // Envia a mensagem para o backend
            const response = await fetch('/mensagens', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(newMessage), // Envia a mensagem em formato JSON
            });

            if (response.ok) {
                // Obtém a mensagem salva do backend e atualiza a lista de mensagens
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
                setMessages(fetchedMessages); // Atualiza a lista de mensagens
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
                {/* Verifica se há mensagens e renderiza com base nisso */}
                {messages.length > 0 ? (
                    messages.map((msg, index) => (
                        <div key={index} className="message-item">
                            <div className="message-body">
                                <strong style={{ color: '#7eabff' }}>{msg.username}</strong>{' '}
                                <span className="message-timestamp">
                                    {new Date(msg.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                                </span>
                                <p>{msg.conteudo}</p>
                            </div>
                        </div>
                    ))
                ) : (
                    <div className="no-messages">Ainda não há mensagens.</div> // Exibe uma mensagem padrão se não houver mensagens
                )}
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
