const tf = require('@tensorflow/tfjs-node');
const mlModelService = require('../services/mlModelService');
const BloodSugarStat = require('../models/BloodSugarStats');
const ApiError = require('../exceptions/ApiError');

let diabetesModel = null; // Cache untuk model diabetes

// Load model diabetes saat inisialisasi
(async () => {
  try {
    diabetesModel = await mlModelService.loadModel('diabetes');
    console.log('Diabetes model loaded successfully.');
  } catch (error) {
    console.error('Failed to load diabetes model:', error);
  }
})();

// POST: Prediksi
exports.predictBloodSugar = async (req, res, next) => {
  try {
    const {
      gender,
      age,
      heart_disease,
      hypertension,
      smoking_history,
      bmi,
      hba1c_level,
      blood_glucose,
    } = req.body;

    if (!diabetesModel) throw ApiError.internal('Diabetes model not loaded yet.');

    // Validasi input
    const input = [
      gender,
      age,
      heart_disease,
      hypertension,
      smoking_history,
      bmi,
      hba1c_level,
      blood_glucose,
    ];

    if (input.some((value) => value == null)) {
      throw ApiError.badRequest('All fields are required.');
    }

    // Proses prediksi
    const tensorInput = tf.tensor2d([input]);
    const prediction = diabetesModel.predict(tensorInput).dataSync()[0];

    const result = prediction > 0.5 ? 'High Risk' : 'Low Risk';

    // Simpan hasil prediksi
    const savedStat = await BloodSugarStat.create({
      user_id: req.user.id,
      gender,
      age,
      heart_disease,
      hypertension,
      smoking_history,
      bmi,
      hba1c_level,
      blood_glucose,
      prediction: result,
    });

    res.status(200).json({
      status: 'success',
      prediction: result,
      data: savedStat,
    });
  } catch (error) {
    next(error);
  }
};

// GET: Riwayat Prediksi
exports.getPredictionHistory = async (req, res, next) => {
  try {
    const history = await BloodSugarStat.findAll({
      where: { user_id: req.user.id },
      order: [['createdAt', 'DESC']],
    });
    res.status(200).json({ status: 'success', data: history });
  } catch (error) {
    next(error);
  }
};

// GET: Detail Prediksi Berdasarkan ID
exports.getPredictionById = async (req, res, next) => {
  try {
    const stat = await BloodSugarStat.findOne({
      where: { id: req.params.id, user_id: req.user.id },
    });
    if (!stat) throw ApiError.notFound('Prediction not found.');
    res.status(200).json({ status: 'success', data: stat });
  } catch (error) {
    next(error);
  }
};
