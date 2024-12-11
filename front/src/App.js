import React from 'react';
import './App.css';  // Certifique-se de que o caminho está correto
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Login from './components/Login';
import Chat from './components/Chat';
import Register from './components/Register';

function App() {
    return (
        <Router>
            <div className="App">
                <div className="headerst">
                    <h1>Chat App</h1>
                </div>
                <Routes>
                    <Route path="/" element={<Navigate to="/login" />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/chat" element={<Chat />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
