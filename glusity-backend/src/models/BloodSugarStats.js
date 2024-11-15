const { DataTypes } = require('sequelize');
const sequelize = require('../config/db');
const User = require('./User');

const BloodSugarStats = sequelize.define('BloodSugarStats', {
    id: {
        type: DataTypes.INTEGER,
        autoIncrement: true,
        primaryKey: true
    },
    user_id: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: User,
            key: 'id'
        },
        onDelete: 'CASCADE'
    },
    blood_sugar_level: {
        type: DataTypes.DECIMAL(5, 2),
        allowNull: false,
        validate: {
            min: 0
        }
    },
    prediction: {
        type: DataTypes.STRING,
        allowNull: false
    },
}, {
    timestamps: true, 
    underscored: true 
});

User.hasMany(BloodSugarStats, { foreignKey: 'user_id' });
BloodSugarStats.belongsTo(User, { foreignKey: 'user_id' });

module.exports = BloodSugarStats;