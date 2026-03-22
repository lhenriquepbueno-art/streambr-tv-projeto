require('dotenv').config();
const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;

// Inicializar Banco e Seed
const { setupDatabase } = require('./models/db');
require('./models/seed');
setupDatabase().then(() => console.log('📂 Banco de Dados SQLite Conectado'));

// Importar Rotas
const authRoutes = require('./routes/authRoutes');
const iptvRoutes = require('./routes/iptvRoutes');

// Middleware
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Conectar Rotas
app.use('/api/auth', authRoutes);
app.use('/api', iptvRoutes);

// Rotas Básicas
app.get('/api/health', (req, res) => {
    res.status(200).json({ status: 'ok', timestamp: new Date().toISOString() });
});

// Logs de requisição
app.use((req, res, next) => {
    console.log(`[${new Date().toISOString()}] ${req.method} ${req.url}`);
    next();
});

// Inicialização do servidor
app.listen(PORT, '0.0.0.0', () => {
    console.log(`==========================================`);
    console.log(`🚀 Servidor StreamBR rodando na porta ${PORT}`);
    console.log(`📍 URL Base: http://localhost:${PORT}`);
    console.log(`==========================================`);
});
