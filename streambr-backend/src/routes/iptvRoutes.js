const express = require('express');
const router = express.Router();
const iptvController = require('../controllers/iptvController');
const authMiddleware = require('../middleware/authMiddleware');

// Rotas protegidas por JWT
router.get('/channels', authMiddleware, iptvController.getChannels);
router.get('/vod', authMiddleware, iptvController.getVod);
router.get('/stream/:id', iptvController.proxyStream); // Rota de Proxy de Streaming
router.post('/channels/sync', iptvController.syncChannels);

module.exports = router;
