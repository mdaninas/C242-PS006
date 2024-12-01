const db = require('../config/db');
const moment = require('moment');

const fetchTrend = async (userId) => {
    try {
        const diabetesQuery = `
            SELECT blood_glucose AS value, created_at 
            FROM diabetes_predictions;
        `;
        const diabetesResult = await db.query(diabetesQuery, [userId]);
        const diabetesData = diabetesResult.rows || diabetesResult;

        const obesityQuery = `
            SELECT weight, created_at 
            FROM obesity_predictions;
        `;
        const obesityResult = await db.query(obesityQuery, [userId]);
        const obesityData = obesityResult.rows || obesityResult;

        // Proses data diabetes
        const glucoseByMonth = {};
        diabetesData.forEach(({ value, created_at }) => {
            const monthKey = moment(created_at).format('YYYY-MM');
            if (!glucoseByMonth[monthKey]) {
                glucoseByMonth[monthKey] = [];
            }
            glucoseByMonth[monthKey].push(value);
        });

        const glucoseMonthlyProgress = Object.keys(glucoseByMonth)
            .sort()
            .map((month) => ({
                month,
                bloodGlucose: glucoseByMonth[month][glucoseByMonth[month].length - 1] 
            }));

        const diabetesTrend = glucoseMonthlyProgress.map((current, index) => {
            if (index === 0) {
                return { ...current, progress: null };
            }

            const previous = glucoseMonthlyProgress[index - 1];
            const progress = current.bloodGlucose > previous.bloodGlucose
                ? 'up'
                : current.bloodGlucose < previous.bloodGlucose
                ? 'down'
                : 'same';

            return { ...current, progress };
        });
        
        // Proses data obesitas
        const weightByMonth = {};
        obesityData.forEach(({ weight, created_at }) => {
            const monthKey = moment(created_at).format('YYYY-MM');
            if (!weightByMonth[monthKey]) {
                weightByMonth[monthKey] = [];
            }
            weightByMonth[monthKey].push(weight);
        });

        const obesityMonthlyProgress = Object.keys(weightByMonth)
            .sort()
            .map((month) => ({
                month,
                weight: weightByMonth[month][weightByMonth[month].length - 1]
            }));

        const obesityTrend = obesityMonthlyProgress.map((current, index) => {
            if (index === 0) {
                return { ...current, progress: null }; 
            }

            const previous = obesityMonthlyProgress[index - 1];
            const progress = current.weight > previous.weight
                ? 'up' 
                : current.weight < previous.weight
                ? 'down'
                : 'same';

            return { ...current, progress };
        });
        
        return {
            obesityTrend,
            diabetesTrend
        };
    } catch (error) {
        throw new Error(`Error fetching trend: ${error.message}`);
    }
};

module.exports = {
    fetchTrend
};