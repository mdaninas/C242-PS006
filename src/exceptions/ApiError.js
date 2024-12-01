class ApiError extends Error {
  constructor(message, statusCode = 400, details = null) {
    super(message);
    this.statusCode = statusCode; 
    this.details = details; 
    this.name = this.constructor.name;
    Error.captureStackTrace(this, this.constructor);
  }

  static badRequest(message, details = null) {
    return new ApiError(message, 400, details);
  }

  static unauthorized(message = 'Unauthorized', details = null) {
    return new ApiError(message, 401, details);
  }

  static forbidden(message = 'Forbidden', details = null) {
    return new ApiError(message, 403, details);
  }

  static notFound(message = 'Not Found', details = null) {
    return new ApiError(message, 404, details);
  }

  static internal(message = 'Internal Server Error', details = null) {
    return new ApiError(message, 500, details);
  }
}

module.exports = ApiError;