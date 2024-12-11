// src/components/Login.js
import React, { useState } from 'react';
import './Login.css'; // importando o CSS
import { Link } from 'react-router-dom'; // Usando o Link para redirecionar


const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // lógica de autenticação aqui
    };

    return (
        <div className="login-container"> {/* adicionando a classe para o container */}
            <form onSubmit={handleSubmit}>
                <h2>Login</h2> {/* título */}
                <input
                    type="text"
                    placeholder="Username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <row>
                    <button type="submit">Login</button>
                    <p><a href="/senha">Esqueci minha senha</a></p> {/* link para a página de recuperação de senha */}

                </row>

                <p>Não possui cadastro? <Link to="/register">Cadastre-se</Link></p> {/* Corrigido */}

            </form>
        </div>
    );
};

export default Login;