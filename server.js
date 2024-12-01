require('dotenv').config();
const express = require('express');
const cors = require('cors');
const cookieParser = require('cookie-parser');
const authRoutes = require('./src/routes/authRoutes');
const healthNewsRoutes = require('./src/routes/healthNewsRoutes');
const userRoutes = require('./src/routes/userRoutes');
const sequelize = require('./src/config/db');
const ApiError = require('./src/exceptions/ApiError');
const errorHandler = require('./src/exceptions/errorHandler');

const allowedOrigin = process.env.ALLOWED_ORIGIN || '*'; // Default: semua origin

const app = express();
app.use(cors({ origin: allowedOrigin }));
app.use(express.json());
app.use(cookieParser());  

// Routes
app.use('/auth', authRoutes);
app.use('/user', userRoutes);
app.use('/news', healthNewsRoutes);

app.use((req, res, next) => {
  next(new ApiError('Not Found', 404)); 
});

app.use(errorHandler); 

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
    
  sequelize.authenticate()
    .then(() => console.log('Database connected successfully'))
    .catch(err => console.error('Database connection error:', err));

  sequelize.sync()
    .then(() => console.log('Database synchronized successfully.'))
    .catch((err) => console.error('Failed to sync database:', err));
});
