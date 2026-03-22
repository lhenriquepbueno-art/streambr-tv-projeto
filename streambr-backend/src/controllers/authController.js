const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');
const { setupDatabase } = require('../models/db');

async function login(req, res) {
    const { username, password } = req.body;
    const db = await setupDatabase();

    try {
        const user = await db.get('SELECT * FROM users WHERE username = ?', [username]);
        if (!user) return res.status(401).json({ message: 'Usuário não encontrado' });

        const validPassword = await bcrypt.compare(password, user.password);
        if (!validPassword) return res.status(401).json({ message: 'Senha incorreta' });

        const accessToken = jwt.sign(
            { id: user.id, username: user.username },
            process.env.JWT_SECRET,
            { expiresIn: '1d' }
        );

        const refreshToken = jwt.sign(
            { id: user.id },
            process.env.JWT_SECRET,
            { expiresIn: '7d' }
        );

        res.json({
            accessToken,
            refreshToken,
            expiresIn: 86400,
            user: {
                id: user.id,
                username: user.username,
                email: user.email,
                plan: user.plan
            }
        });
    } catch (error) {
        res.status(500).json({ message: 'Erro no servidor' });
    }
}

async function refresh(req, res) {
    const { refreshToken } = req.body;
    if (!refreshToken) return res.status(401).json({ message: 'Refresh token não fornecido' });

    try {
        const payload = jwt.verify(refreshToken, process.env.JWT_SECRET);
        const db = await setupDatabase();
        const user = await db.get('SELECT * FROM users WHERE id = ?', [payload.id]);

        if (!user) return res.status(401).json({ message: 'Usuário inválido' });

        const newAccessToken = jwt.sign(
            { id: user.id, username: user.username },
            process.env.JWT_SECRET,
            { expiresIn: '1d' }
        );

        res.json({ accessToken: newAccessToken });
    } catch (err) {
        res.status(403).json({ message: 'Refresh token inválido' });
    }
}

async function verifyPin(req, res) {
    const { pin } = req.body;
    // PIN fixo para desenvolvimento: 0000
    if (pin === '0000') {
        res.json({
            adultToken: 'secured_adult_session_token_' + Date.now(),
            message: 'PIN Correto'
        });
    } else {
        res.status(401).json({ message: 'PIN Incorreto' });
    }
}

module.exports = { login, refresh, verifyPin };
