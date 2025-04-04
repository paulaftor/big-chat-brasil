import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Register.css';

const Register = () => {
    const [nome, setNome] = useState('');
    const [telefone, setTelefone] = useState('');
    const [cpfResponsavel, setCpfResponsavel] = useState('');
    const [cnpj, setCnpj] = useState('');
    const [plano, setPlano] = useState('PREPAGO');
    const [creditos, setCreditos] = useState(0);
    const [limiteConsumo, setLimiteConsumo] = useState(0);
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const clienteData = {
            nome,
            telefone,
            cpfResponsavel,
            cnpj,
            plano,
            creditos,
            limiteConsumo,
            email,
            senha
        };

        try {
            const response = await axios.post('http://localhost:8080/clientes/cadastrar', clienteData, {
                headers: { 'Content-Type': 'application/json' },
            });

            console.log('Cliente cadastrado com sucesso:', response.data);
            localStorage.setItem('loggedInUser', JSON.stringify(response.data));
            setErrorMessage('');
            navigate('/chat');
        } catch (error) {
            console.error('Erro ao cadastrar:', error);
            setErrorMessage('Erro: ' + (error.response?.data?.message || 'Não foi possível cadastrar o cliente.'));
        }
    };

    return (
        <div className="register-container">
            <form onSubmit={handleSubmit}>
                <h2>Cadastro de Cliente</h2>
                {errorMessage && <div className="error-message">{errorMessage}</div>}

                <input type="text" placeholder="Nome" value={nome} onChange={(e) => setNome(e.target.value)} required />
                <input type="text" placeholder="Telefone" value={telefone} onChange={(e) => setTelefone(e.target.value)} required />
                <input type="text" placeholder="CPF do Responsável" value={cpfResponsavel} onChange={(e) => setCpfResponsavel(e.target.value)} required />
                <input type="text" placeholder="CNPJ" value={cnpj} onChange={(e) => setCnpj(e.target.value)} required />
                <select value={plano} onChange={(e) => setPlano(e.target.value)} required>
                    <option value="PREPAGO">Pré-pago</option>
                    <option value="POSPAGO">Pós-pago</option>
                </select>
                <input type="number" step="0.01" placeholder="Créditos" value={creditos} onChange={(e) => setCreditos(parseFloat(e.target.value))} required />
                <input type="number" step="0.01" placeholder="Limite de Consumo" value={limiteConsumo} onChange={(e) => setLimiteConsumo(parseFloat(e.target.value))} required />
                <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                <input type="password" placeholder="Senha" value={senha} onChange={(e) => setSenha(e.target.value)} required />

                <button type="submit">Cadastrar</button>
            </form>
        </div>
    );
};

export default Register;
