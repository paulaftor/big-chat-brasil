import React, { useState } from 'react';
import './Login.css'; // importando o CSS
import { Link } from 'react-router-dom'; // Usando o Link para redirecionar

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');  // Limpa o erro antes de tentar enviar
        setIsLoading(true);  // Ativa o carregamento

        try {
            const response = await fetch('http://seu-backend.com/api/usuarios/autenticar', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ input: username, senha: password }),
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('token', data.token);
                localStorage.setItem('userId', data.userId);
                localStorage.setItem('username', data.username);
                window.location.href = '/chat';  // Redireciona após login bem-sucedido
            } else {
                setError('Credenciais inválidas');
            }
        } catch (error) {
            setError('Erro durante a autenticação');
            console.error('Erro durante a autenticação:', error);
        } finally {
            setIsLoading(false);  // Desativa o carregamento após a requisição
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
                    value={password}
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
