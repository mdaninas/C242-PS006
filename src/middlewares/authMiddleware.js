const jwt = require('jsonwebtoken');
const ApiError = require('../exceptions/ApiError');
const { isTokenBlacklisted } = require('../services/blacklistToken');

const authMiddleware = (req, res, next) => {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      throw ApiError.unauthorized('Missing Bearer Token');
    }

    const accessToken = authHeader.split(' ')[1]; 
    if (isTokenBlacklisted(accessToken)) {
      throw ApiError.unauthorized('Access token has been blacklisted');
    }

    const decoded = jwt.verify(accessToken, process.env.JWT_SECRET);
    req.userId = decoded.userId;
    next();
  } catch (err) {
    next(err instanceof ApiError ? err : ApiError.unauthorized());
  }
};

module.exports = authMiddleware;