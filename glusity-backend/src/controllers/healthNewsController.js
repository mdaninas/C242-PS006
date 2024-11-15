const { fetchHealthNews } = require('../services/healthNewsService');
const ApiError = require('../exceptions/ApiError');


const getHealthNews = async (req, res, next) => {
  try {
    const articles = await fetchHealthNews();
    res.json(articles);
  } catch (error) {
    next(new ApiError('Failed to fetch health news', 500));
  }
};

module.exports = { getHealthNews };