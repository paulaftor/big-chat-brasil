// src/App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './components/Login';
import Chat from './components/Chat'; // Certifique-se de que o componente Register existe
import Register from './components/Register'; // Certifique-se de que o componente Register existe

function App() {
    return (
        <Router>
            <div className="App">
                <h1>Chat App</h1>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/chat" element={<Chat />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
