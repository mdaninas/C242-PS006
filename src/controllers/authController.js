const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const User = require('../models/User');
const validator = require('validator');
const { addTokenToBlacklist, isTokenBlacklisted } = require('../services/blacklistToken');

exports.isTokenBlacklisted = (req, res) => {
  const accessToken = req.headers.authorization?.split(' ')[1]; 
  if (!accessToken) {
    return res.status(400).json({ message: 'Access token is missing' });
  }
  const blacklisted = isTokenBlacklisted(accessToken);
  res.status(200).json({ blacklisted });
};

// Register handler
exports.register = async (req, res) => {
  const { name, email, password, confirmPassword } = req.body;

  if (!name || !email || !password || !confirmPassword) {
    return res.status(400).json({ message: 'All fields are required' });
  }
  if (!validator.isEmail(email)) {
    return res.status(400).json({ message: 'Invalid email format' });
  }
  if (password !== confirmPassword) {
    return res.status(400).json({ message: 'Passwords do not match' });
  }

  try {
    const hashedPassword = await bcrypt.hash(password, 6);
    const user = await User.create({ name, email, password: hashedPassword });
    res.status(201).json({ message: 'User registered successfully', userId: user.id });
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: 'Failed to register user' });
  }
};

// Helper untuk membuat token
const generateToken = (userId, expiresIn) => {
  return jwt.sign({ userId }, process.env.JWT_SECRET, { expiresIn });
};

// Login handler
exports.login = async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res.status(400).json({ message: 'Email and password are required' });
  }

  try {
    const user = await User.findOne({ where: { email } });
    if (!user || !(await bcrypt.compare(password, user.password))) {
      return res.status(401).json({ message: 'Invalid credentials' });
    }

    // Generate tokens
    const accessToken = generateToken(user.id, '15m'); 
    const refreshToken = generateToken(user.id, '72h'); 

    res.cookie('refreshToken', refreshToken, { httpOnly: true, secure: true }); 
    res.status(200).json({
      message: 'Logged in successfully',
      accessToken,
      userId: user.id,
    });
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: 'Failed to login' });
  }
};

// Refresh token handler
exports.refreshToken = async (req, res) => {
  const refreshToken = req.cookies.refreshToken;

  if (!refreshToken) {
    return res.status(401).json({ message: 'Refresh token not provided' });
  }

  if (isTokenBlacklisted(refreshToken)) {
    return res.status(401).json({ message: 'Refresh token has been blacklisted' });
  }

  try {
    const decoded = jwt.verify(refreshToken, process.env.JWT_SECRET);
    addTokenToBlacklist(refreshToken);

    const newAccessToken = generateToken(decoded.userId, '15m');
    const newRefreshToken = generateToken(decoded.userId, '72h');

    res.cookie('refreshToken', newRefreshToken, { httpOnly: true, secure: true });
    res.status(200).json({
      accessToken: newAccessToken,
    });
  } catch (err) {
    console.error(err);
    res.status(401).json({ message: 'Invalid or expired refresh token' });
  }
};


//Logout Handler
exports.logout = (req, res) => {
  const refreshToken = req.cookies.refreshToken;
  const accessToken = req.headers.authorization?.split(' ')[1]; 

  if (refreshToken) {
    addTokenToBlacklist(refreshToken); 
  }

  if (accessToken) {
    addTokenToBlacklist(accessToken); 
  }

  res.clearCookie('refreshToken'); 
  res.status(200).json({ message: 'Logged out successfully' });
};