// src/components/Register.js
import React, { useState } from 'react';
import axios from 'axios'; // Importe o axios
import './Register.css'; // CSS para o estilo

const Register = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');

   const handleSubmit = async (e) => {
       e.preventDefault();
       const userData = {
           username,
           password,
           email,
       };

       // Validações adicionais
       if (!username || !password || !email) {
           console.error('Todos os campos são obrigatórios.');
           return; // Interrompe a execução se algum campo estiver vazio
       }

       // Validação de formato de email (opcional)
       const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
       if (!emailRegex.test(email)) {
           console.error('Email inválido.');
           return; // Interrompe a execução se o email for inválido
       }

       try {
           const response = await axios.post('http://localhost:8080/api/usuarios/cadastrar', userData, {
               headers: {
                   'Content-Type': 'application/json',
                   'Authorization': 'Basic ' + btoa('user:password')
               },
           });
           console.log(response.data);
       } catch (error) {
           console.error('Error:', error);
       }
   };


    return (
        <div className="register-container">
            <form onSubmit={handleSubmit}>
                <h2>Cadastro</h2>
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
