const express = require('express');
const bloodSugarController = require('../controllers/bloodSugarController');
const authMiddleware = require('../middlewares/authMiddleware');

const router = express.Router();

router.post('/predict', authMiddleware, bloodSugarController.predictBloodSugar);
router.get('/history', authMiddleware, bloodSugarController.getPredictionHistory);
router.get('/history/:id', authMiddleware, bloodSugarController.getPredictionById);

module.exports = router;
