const ApiError = require('./ApiError');

const errorHandler = (err, req, res, next) => {
  const statusCode = err instanceof ApiError ? err.statusCode : 500;
  const message = err.message || 'Internal Server Error';
  const details = err.details || null;


  console.error(`[ERROR] ${err.name}: ${message}`, details || err.stack);

  res.status(statusCode).json({
    error: {
      message,
      statusCode,
      details: process.env.NODE_ENV === 'development' ? details : undefined, 
    },
  });
};

module.exports = errorHandler;