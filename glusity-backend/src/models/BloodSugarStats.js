const { DataTypes } = require('sequelize');
const sequelize = require('../config/db');
const User = require('./User');

const BloodSugarStats = sequelize.define('BloodSugarStats', {
    gender: {
        type: DataTypes.BOOLEAN, // 0 atau 1 untuk binary (misalnya: 0 = Female, 1 = Male)
        allowNull: false
    },
    age: {
        type: DataTypes.INTEGER, // integer untuk umur
        allowNull: false
    },
    heart_disease: { // binary
        type: DataTypes.BOOLEAN, // Boolean untuk data binary (true/false)
        allowNull: false
    },
    hypertension: { // binary
        type: DataTypes.BOOLEAN, // Boolean untuk data binary (true/false)
        allowNull: false
    },
    smoking_history: { // binary
        type: DataTypes.BOOLEAN, // Binary (true/false)
        allowNull: false
    },
    bmi: { // Body Mass Index
        type: DataTypes.DECIMAL(5, 2), // Desimal, karena BMI biasanya berbentuk seperti 25.32
        allowNull: false,
        validate: {
            min: 0 // BMI tidak mungkin negatif
        }
    },
    hba1c_level: { // HbA1c Level
        type: DataTypes.DECIMAL(4, 2), // Nilai HbA1c biasanya dalam rentang desimal, seperti 5.6
        allowNull: false,
        validate: {
            min: 0
        }
    },
    blood_glucose: { // Blood Glucose Level
        type: DataTypes.DECIMAL(6, 2), // Desimal untuk angka gula darah (mg/dL)
        allowNull: false,
        validate: {
            min: 0
        }
    }
});

User.hasMany(BloodSugarStats, { foreignKey: 'user_id' });
BloodSugarStats.belongsTo(User, { foreignKey: 'user_id' });

module.exports = BloodSugarStats;