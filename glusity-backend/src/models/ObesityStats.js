const { DataTypes } = require('sequelize');
const sequelize = require('../config/db');
const User = require('./User');

const ObesityStats = sequelize.define('ObesityStats', {
    gender: { // 0 untuk Female, 1 untuk Male
        type: DataTypes.BOOLEAN,
        allowNull: false
    },
    age: { // Umur dalam tahun
        type: DataTypes.FLOAT,
        allowNull: false
    },
    height: { // Tinggi dalam cm
        type: DataTypes.FLOAT,
        allowNull: false
    },
    weight: { // Berat dalam kg
        type: DataTypes.FLOAT,
        allowNull: false
    },
    fcvc: { // Frekuensi konsumsi sayuran
        type: DataTypes.FLOAT,
        allowNull: false
    },
    ncp: { // Konsumsi makanan per hari
        type: DataTypes.FLOAT,
        allowNull: false,
        validate: {
            min: 1,
            max: 3
        }
    },
    ch2o: { // Asupan air per hari dalam liter
        type: DataTypes.FLOAT,
        allowNull: false,
        validate: {
            min: 0
        }
    },
    faf: { // Frekuensi aktivitas fisik per minggu
        type: DataTypes.FLOAT,
        allowNull: false,
        validate: {
            min: 0
        }
    },
    tue: { // Waktu penggunaan teknologi per hari
        type: DataTypes.FLOAT,
        allowNull: false,
        validate: {
            min: 0
        }
    }
}, {
    timestamps: true,
    underscored: true
});

User.hasMany(ObesityStats, { foreignKey: 'user_id' });
ObesityStats.belongsTo(User, { foreignKey: 'user_id' });

module.exports = ObesityStats;
