package manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.HealthData;
import util.DatabaseConnection;

/**
 * In this basic version of the
 * manager.RecommendationSystem class, complete the generateRecommendations to take a
 * model.HealthData object as input and generates recommendations based on the user's heart rate and step count.
 * You can also expand this class to include more health data analysis and generate more specific
 * recommendations based on the user's unique health profile
 * NOTE:
 * To integrate this class into your application, you'll need to pass the model.HealthData object to the generateRecommendations method
 * and store the generated recommendations in the recommendations table in the database.
 */

public class RecommendationSystem {
    private static final int MIN_HEART_RATE = 60;
    private static final int MAX_HEART_RATE = 90;
    private static final int MIN_STEPS = 7000;
    private static final double BMI_THRESHOLD = 25.0;


    public List<String> getRecommendationsForUser(int userId) {
        List<String> recommendations = new ArrayList<>();
        String sql = "SELECT recommendation_text FROM recommendations WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    recommendations.add(rs.getString("recommendation_text"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recommendations;
    }
    // Generate recommendations based on health data
    public List<String> generateRecommendations(HealthData healthData) {
        List<String> recommendations = new ArrayList<>();

        int heartRate = healthData.getHeartRate();
        if (heartRate < MIN_HEART_RATE) {
            recommendations.add("Your heart rate is below the recommended range. Consider increasing your physical activity to improve your cardiovascular health.");
        } else if (heartRate > MAX_HEART_RATE) {
            recommendations.add("Your heart rate is above the recommended range. It is advised to consult a doctor for further guidance.");
        }

        int steps = healthData.getSteps();
        if (steps < MIN_STEPS) {
            recommendations.add("You are not reaching the recommended daily step count. Try to incorporate more walking or other physical activities into your daily routine.");
        }

        double bmi = calculateBMI(healthData.getWeight(), healthData.getHeight());
        if (bmi > BMI_THRESHOLD) {
            recommendations.add("Your Body Mass Index (BMI) is higher than normal, which may indicate overweight. It is recommended to consult a dietitian and review your diet and physical activity regimen.");
        }


        return recommendations;
    }

    // Calculate BMI
    private double calculateBMI(double weight, double height) {
        return weight / (height * height);
    }

    // Save recommendations
    public boolean saveRecommendation(int patientId, String recommendationText) {
        String sql = "INSERT INTO recommendations (user_id, recommendation_text, date) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, patientId);
            preparedStatement.setString(2, recommendationText);
            preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

