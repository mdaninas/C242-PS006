const express = require('express');
const router = express.Router();
const healthNewsController = require('../controllers/healthNewsController');
const authMiddleware = require('../middlewares/authMiddleware'); 

router.get('/', authMiddleware, healthNewsController.getHealthNews);

module.exports = router;
