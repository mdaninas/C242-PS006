const bcrypt = require('bcrypt');
const User = require('../models/User');
const ApiError = require('../exceptions/ApiError');

// Update user info
exports.updateUser = async (req, res, next) => {
  const { name, email, password } = req.body;
  const userId = req.userId;

  if (!name && !email && !password) {
    return res.status(400).json({ message: 'At least one field (name, email, or password) is required' });
  }

  const updates = {};
  if (name) updates.name = name;
  if (email) updates.email = email;
  if (password) updates.password = await bcrypt.hash(password, 10);

  try {
    await User.update(updates, { where: { id: userId } });
    res.status(200).json({ message: 'User information updated successfully' });
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: 'Failed to update user information' });
  }
};

// Get user profile
exports.getProfile = async (req, res, next) => {
  const userId = req.userId; 

  try {
    const user = await User.findByPk(userId, {
      attributes: ['id', 'name', 'email'] 
    });

    if (!user) {
      throw new ApiError('User not found', 404);
    }

    res.status(200).json({
      message: 'User profile fetched successfully',
      user: {
        id: user.id,
        name: user.name,
        email: user.email,
      }
    });
  } catch (error) {
    next(error); 
  }
};