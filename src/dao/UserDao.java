package dao;

import model.User;
import org.mindrot.jbcrypt.BCrypt;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    // create user
    public boolean createUser(User user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        String sql = "INSERT INTO users (first_name, last_name, email, password, is_doctor) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, hashedPassword);
            preparedStatement.setBoolean(5, user.isDoctor());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // get user by id
    public User getUserById(int id) {
        User user = null;
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    user = new User(rs.getInt("id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getString("email"),
                            rs.getString("password"), rs.getBoolean("is_doctor"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // get user by email
    public User getUserByEmail(String email) {
        User user = null;
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, email);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    user = new User(rs.getInt("id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getString("email"),
                            rs.getString("password"), rs.getBoolean("is_doctor"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // update user
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ?, is_doctor = ? WHERE id = ?";
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, hashedPassword);
            preparedStatement.setBoolean(5, user.isDoctor());
            preparedStatement.setInt(6, user.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // delete user
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

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

    // verify password
    public boolean verifyPassword(String email, String password) {
        String sql = "SELECT password FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password");
                    return BCrypt.checkpw(password, hashedPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // get patients by doctor id
    public List<User> getPatientsByDoctorId(int doctorId) {
        List<User> patients = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE doctor_id = ? AND is_doctor = false";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, doctorId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    User user = new User(rs.getInt("id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getString("email"),
                            rs.getString("password"), rs.getBoolean("is_doctor"));
                    patients.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    // assign patient to doctor
    public boolean assignPatientToDoctor(int patientId, int doctorId) {
        String sql = "UPDATE users SET doctor_id = ? WHERE id = ? AND is_doctor = false";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setInt(2, patientId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
