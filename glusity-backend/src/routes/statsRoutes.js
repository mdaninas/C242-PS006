const express = require('express');
const statsController = require('../controllers/statsController');
const authMiddleware = require('../middlewares/authMiddleware');
const router = express.Router();

router.post('/check', authMiddleware, statsController.checkBloodSugar);
router.get('/:id', authMiddleware, statsController.getStatsById);

module.exports = router;