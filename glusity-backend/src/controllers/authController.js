const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const User = require('../models/User');
const validator = require('validator');
const { addTokenToBlacklist } = require('../services/blacklistToken');

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
    
    // Token tanpa kedaluwarsa
    const token = jwt.sign({ userId: user.id }, process.env.JWT_SECRET);
    res.cookie('token', token, { httpOnly: true });
    res.status(200).json({ message: 'Logged in successfully', token });
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: 'Failed to login' });
  }
};

//Logout Handler
exports.logout = (req, res) => {
  const token = req.headers.authorization?.split(' ')[1];

  if (!token) {
    return res.status(400).json({ message: 'Cannot logout because no token was provided. Please login first.' });
  }
  addTokenToBlacklist(token);
  res.clearCookie('token');
  res.status(200).json({ message: 'Logged out successfully' });
};