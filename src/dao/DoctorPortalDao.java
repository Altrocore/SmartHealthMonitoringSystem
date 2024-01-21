package dao;

import model.Doctor;
import model.HealthData;
import model.User;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorPortalDao {
    private final UserDao userDao;
    private final HealthDataDao healthDataDao;

    public DoctorPortalDao() {
        userDao = new UserDao();
        healthDataDao = new HealthDataDao();
    }

    /**
     * Retrieves a doctor based on the provided patient ID.
     *
     * @param  patientId  the ID of the patient
     * @return            the model.Doctor object representing the doctor,
     *                    or null if no doctor is found
     */
    public Doctor getDoctorByPatientId(int patientId) {
        String sql = "SELECT u.id, u.first_name, u.last_name, u.email, u.password, u.is_doctor, d.specialization, d.medicallicensenumber " +
                "FROM users u " +
                "INNER JOIN doctor_patient dp ON u.id = dp.doctor_id " +
                "INNER JOIN doctors d ON u.id = d.user_id " +
                "WHERE dp.patient_id = ?";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, patientId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    User doctorUser = new User(rs.getInt("id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getString("email"),
                            rs.getString("password"), rs.getBoolean("is_doctor"));
                    Doctor doctor = new Doctor(doctorUser.getId(), doctorUser.getFirstName(),
                            doctorUser.getLastName(), doctorUser.getEmail(),
                            doctorUser.getPassword(), doctorUser.isDoctor());
                    doctor.setSpecialization(rs.getString("specialization"));
                    doctor.setMedicalLicenseNumber(rs.getString("medicallicensenumber"));
                    return doctor;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Retrieves a list of patients associated with a specific doctor ID.
     *
     * @param  doctorId  the ID of the doctor
     * @return           a list of model.User objects representing the patients
     */
    public List<User> getPatientsByDoctorId(int doctorId) {
        List<User> patients = new ArrayList<>();
        String sql = "SELECT u.* FROM users u INNER JOIN doctor_patient dp ON u.id = dp.patient_id WHERE dp.doctor_id = ?";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, doctorId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    User patient = new User(rs.getInt("id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getString("email"),
                            rs.getString("password"), rs.getBoolean("is_doctor"));
                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    /**
     * Retrieve health data for a given patient ID.
     *
     * @param patientId The ID of the patient
     * @return A list of health data objects for the patient
     */
    public List<HealthData> getHealthDataByPatientId(int patientId) {
        return healthDataDao.getHealthDataByUserId(patientId);
    }

    /**
     * Inserts a patient into the doctor_patient table to establish a connection between a doctor and a patient.
     *
     * @param  patientId  the ID of the patient to be assigned
     * @param  doctorId   the ID of the doctor to assign the patient to
     * @return            true if the patient was successfully assigned to the doctor, false otherwise
     */
    public boolean assignPatientToDoctor(int patientId, int doctorId) {
        String sql = "INSERT INTO doctor_patient (doctor_id, patient_id) VALUES (?, ?)";

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

    /**
     * Removes a patient from a doctor's list of patients.
     *
     * @param  patientId  the ID of the patient to be removed
     * @param  doctorId   the ID of the doctor
     * @return            true if the patient was successfully removed, false otherwise
     */
    public boolean removePatientFromDoctor(int patientId, int doctorId) {
        String sql = "DELETE FROM doctor_patient WHERE doctor_id = ? AND patient_id = ?";

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
