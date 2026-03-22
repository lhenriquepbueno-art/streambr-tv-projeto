const sqlite3 = require('sqlite3');
const { open } = require('sqlite');
const path = require('path');

async function setupDatabase() {
    const db = await open({
        filename: process.env.DATABASE_PATH || './src/database.sqlite',
        driver: sqlite3.Database
    });

    // Tabela de Usuários
    await db.exec(`
        CREATE TABLE IF NOT EXISTS users (
            id TEXT PRIMARY KEY,
            username TEXT UNIQUE,
            password TEXT,
            email TEXT,
            plan TEXT DEFAULT 'free',
            created_at DATETIME DEFAULT CURRENT_TIMESTAMP
        )
    `);

    // Tabela de Canais (Cache do Servidor)
    await db.exec(`
        CREATE TABLE IF NOT EXISTS channels (
            id TEXT PRIMARY KEY,
            name TEXT,
            logo TEXT,
            group_name TEXT,
            quality TEXT,
            stream_url TEXT,
            hls_url TEXT,
            is_adult INTEGER DEFAULT 0
        )
    `);

    // Inserir usuário padrão se não existir (admin / admin123)
    const bcrypt = require('bcryptjs');
    const adminExists = await db.get('SELECT * FROM users WHERE username = ?', ['admin']);
    if (!adminExists) {
        const hashedPassword = await bcrypt.hash('admin123', 10);
        await db.run(
            'INSERT INTO users (id, username, password, email, plan) VALUES (?, ?, ?, ?, ?)',
            ['1', 'admin', hashedPassword, 'admin@streambr.com', 'premium']
        );
        console.log('✅ Usuário padrão criado: admin / admin123');
    }

    return db;
}

module.exports = { setupDatabase };
