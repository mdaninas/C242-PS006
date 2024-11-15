const mlModelService = require('../services/mlModelService');
const BloodSugarStat = require('../models/BloodSugarStats');
const ApiError = require('../exceptions/ApiError');

exports.checkBloodSugar = async (req, res, next) => {
  try {
    const { bloodSugarLevel } = req.body;
    if (!bloodSugarLevel) throw ApiError.badRequest('Blood sugar level is required');
    const prediction = await mlModelService.predictBloodSugar(bloodSugarLevel);

    const suggestion = prediction === 'High' 
      ? 'Please consult a doctor' 
      : 'Your blood sugar level is within a normal range';

    const savedStat = await BloodSugarStat.create({
      userId: req.user.id,
      prediction,
      suggestion,
    });

    res.status(200).json({ status: 'success', prediction, savedStat });
  } catch (error) {
    next(error);
  }
};

exports.getStatsById = async (req, res, next) => {
  try {
    const stats = await BloodSugarStat.findByPk(req.params.id);
    if (!stats) throw ApiError.notFound('Statistics not found');
    res.status(200).json({ status: 'success', data: stats });
  } catch (error) {
    next(error);
  }
};