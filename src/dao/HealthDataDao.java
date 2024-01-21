package dao;

import model.HealthData;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HealthDataDao {

    /**
     * Creates a new health data entry in the database.
     *
     * @param  healthData  the health data object to be inserted
     * @return             true if the insertion is successful, false otherwise
     */
    public boolean createHealthData(HealthData healthData) {
        String sql = "INSERT INTO health_data (user_id, weight, height, steps, heart_rate, date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, healthData.getUserId());
            preparedStatement.setDouble(2, healthData.getWeight());
            preparedStatement.setDouble(3, healthData.getHeight());
            preparedStatement.setInt(4, healthData.getSteps());
            preparedStatement.setInt(5, healthData.getHeartRate());

            java.sql.Date sqlDate = java.sql.Date.valueOf(healthData.getDate());
            preparedStatement.setDate(6, sqlDate);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Retrieves health data from the database based on the provided ID.
     *
     * @param  id  the ID of the health data to retrieve
     * @return     the model.HealthData object representing the retrieved data, or null if not found
     */
    public HealthData getHealthDataById(int id) {
        HealthData healthData = null;
        String sql = "SELECT * FROM health_data WHERE id = ?";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    healthData = new HealthData(rs.getInt("id"), rs.getInt("user_id"),
                            rs.getDouble("weight"), rs.getDouble("height"),
                            rs.getInt("steps"), rs.getInt("heart_rate"),
                            rs.getString("date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return healthData;
    }

    /**
     * Retrieves a list of model.HealthData objects associated with a specific user ID.
     *
     * @param  userId  the ID of the user
     * @return         a list of model.HealthData objects
     */
    public List<HealthData> getHealthDataByUserId(int userId) {
        List<HealthData> healthDataList = new ArrayList<>();
        String sql = "SELECT * FROM health_data WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    HealthData healthData = new HealthData(
                            rs.getInt("id"),
                            userId,
                            rs.getDouble("weight"),
                            rs.getDouble("height"),
                            rs.getInt("steps"),
                            rs.getInt("heart_rate"),
                            rs.getString("date")
                    );
                    healthDataList.add(healthData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return healthDataList;
    }

    /**
     * Updates the health data in the database with the provided model.HealthData object.
     *
     * @param  healthData   the model.HealthData object containing the updated health data
     * @return              true if the health data was successfully updated, false otherwise
     */
    public boolean updateHealthData(HealthData healthData) {
        String sql = "UPDATE health_data SET weight = ?, height = ?, steps = ?, heart_rate = ?, date = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setDouble(1, healthData.getWeight());
            preparedStatement.setDouble(2, healthData.getHeight());
            preparedStatement.setInt(3, healthData.getSteps());
            preparedStatement.setInt(4, healthData.getHeartRate());
            preparedStatement.setString(5, healthData.getDate());
            preparedStatement.setInt(6, healthData.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes health data from the database based on the provided ID.
     *
     * @param  id  the ID of the health data to be deleted
     * @return     true if the data is successfully deleted, false otherwise
     */
    public boolean deleteHealthData(int id) {
        String sql = "DELETE FROM health_data WHERE id = ?";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
