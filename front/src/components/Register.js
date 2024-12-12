import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Register.css';

const Register = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

const handleSubmit = async (e) => {
    e.preventDefault();
    const userData = { username, senha: password, email };

    try {
        const response = await axios.post('http://localhost:8080/usuarios/cadastrar', userData, {
            headers: {
                'Content-Type': 'application/json',
            },
        });

        console.log('Usuário cadastrado com sucesso:', response.data);
        localStorage.setItem('loggedInUser', JSON.stringify(response.data));
        setErrorMessage('');
        navigate('/chat');
    } catch (error) {
        if (error.response) {
            // Erro com resposta do servidor
            console.error('Erro ao cadastrar:', error.response.data);
            setErrorMessage('Erro: ' + (error.response.data.message || 'Não foi possível cadastrar o usuário.'));
        } else {
            // Erro de rede ou outro problema
            console.error('Erro na requisição:', error);
            setErrorMessage('Erro ao conectar com o servidor.');
        }
    }
};


    return (
        <div className="register-container">
            <form onSubmit={handleSubmit}>
                <h2>Crie sua conta</h2>
                {errorMessage && <div className="error-message">{errorMessage}</div>}
                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <button type="submit">Cadastrar</button>
            </form>
        </div>
    );
};

export default Register;
