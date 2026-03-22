const express = require('express');
const router = express.Router();
const authController = require('../controllers/authController');

router.post('/login', authController.login);
router.post('/refresh', authController.refresh);
router.post('/pin', authController.verifyPin);

module.exports = router;
