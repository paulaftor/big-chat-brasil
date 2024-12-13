import React, { useState } from 'react';
import axios from 'axios';
import './Login.css'; // importando o CSS
import { Link } from 'react-router-dom'; // Usando o Link para redirecionar
import { useNavigate } from 'react-router-dom'; // Importando o useNavigate

const Login = () => {
    const [username, setUsername] = useState('');
    const [senha, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    // Usando useNavigate para redirecionamento
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const userData = {input: username, senha};

        try {
            const response = await axios.post('http://localhost:8080/usuarios/autenticar', userData, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            console.log('Usuário autenticado com sucesso:', response.data);
            localStorage.setItem('loggedInUser', JSON.stringify(response.data));
            setError(''); // Limpa a mensagem de erro
            navigate('/chat'); // Redireciona para a página de chat
        } catch (error) {
            if (error.response) {
                console.error('Erro ao logar:', error.response.data);
                setError('Erro: ' + (error.response.data.message || 'Não foi possível fazer login do usuário.'));
            } else {
                // Erro de rede ou outro problema
                console.error('Erro na requisição:', error);
                setError('Erro ao conectar com o servidor.');
            }
        }
    };

    return (
        <div className="login-container">
            <form onSubmit={handleSubmit}>
                <h2>Login</h2>

                {/* Campo de usuário */}
                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />

                {/* Campo de senha */}
                <input
                    type="password"
                    placeholder="Password"
                    value={senha}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />

                {/* Botão de login */}
                <button type="submit" disabled={isLoading}>
                    {isLoading ? 'Carregando...' : 'Login'}
                </button>

                {/* Exibe mensagem de erro se houver */}
                {error && <div className="error-message">{error}</div>}

                {/* Links de navegação */}
                <Link to="/senha" className="forgot-password-link">Esqueci minha senha</Link>
                <Link to="/register" className="forgot-password-link">Não é cadastrado? Cadastre-se</Link>
            </form>
        </div>
    );
};

export default Login;
