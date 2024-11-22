const express = require('express');
const router = express.Router();
const obesityController = require('../controllers/obesityController');
const authMiddleware = require('../middlewares/authMiddleware');

router.post('/predict', authMiddleware, obesityController.predictObesity);
router.get('/history', authMiddleware, obesityController.getPredictionHistory);
router.get('/history/:id', authMiddleware, obesityController.getPredictionById);

module.exports = router;