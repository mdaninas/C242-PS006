const tf = require('@tensorflow/tfjs-node');
const ObesityStats = require('../models/ObesityStats');
const ApiError = require('../exceptions/ApiError');
const mlModelService = require('../services/mlModelService');

let obesityModel = null;

// Load model obesitas
(async () => {
  try {
    obesityModel = await mlModelService.loadModel('obesity');
    console.log('Obesity model loaded successfully.');
  } catch (error) {
    console.error('Failed to load obesity model:', error);
  }
})();

// POST: Prediksi Obesitas
exports.predictObesity = async (req, res, next) => {
    const { gender, age, height, weight, fcvc, ncp, ch2o, faf, tue } = req.body;
    if (!obesityModel) {
      return next(ApiError.internal('Obesity model not loaded yet.'));
    }
  
    const input = [gender, age, height, weight, fcvc, ncp, ch2o, faf, tue];
    if (input.some((value) => value == null)) {
      return next(ApiError.badRequest('All fields are required.'));
    }
  
    const tensorInput = tf.tensor2d([input]);
    const predictions = obesityModel.predict(tensorInput).dataSync();
    const resultIndex = predictions.indexOf(Math.max(...predictions)); // Mencari index dengan nilai tertinggi
  
    try {
      const savedStat = await ObesityStats.create({
        user_id: req.user.id,
        gender,
        age,
        height,
        weight,
        fcvc,
        ncp,
        ch2o,
        faf,
        tue,
        prediction: resultIndex
      });
  
      res.status(200).json({ status: 'success', prediction: resultIndex, data: savedStat });
    } catch (error) {
      next(error);
    }
  };

  // GET: Riwayat Prediksi
exports.getPredictionHistory = async (req, res, next) => {
    try {
      const history = await ObesityStats.findAll({
        where: { user_id: req.user.id },
        order: [['createdAt', 'DESC']]
      });
      res.status(200).json({ status: 'success', data: history });
    } catch (error) {
      next(error);
    }
  };

// GET: Detail Prediksi Berdasarkan ID
exports.getPredictionById = async (req, res, next) => {
    try {
      const stat = await ObesityStats.findOne({
        where: { id: req.params.id, user_id: req.user.id }
      });
      if (!stat) return next(ApiError.notFound('Prediction not found.'));
  
      res.status(200).json({ status: 'success', data: stat });
    } catch (error) {
      next(error);
    }
  };