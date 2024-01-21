package manager;

import model.MedicineReminder;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The manager.MedicineReminderManager class should have methods to add reminders, get reminders
 *  1. for a specific user, and
 *  2. get reminders that are DUE for a specific user.
 *
 *  You'll need to integrate this class with your application and database logic to
 *  1. store,
 *  2. update, and
 *  3. delete reminders as needed.
 */

public class MedicineReminderManager {
    private final List<MedicineReminder> reminders;

    public MedicineReminderManager() {
        reminders = new ArrayList<>();
    }

    // Add a reminder
    public void addReminder(MedicineReminder reminder) {
        String sql = "INSERT INTO medicine_reminders (user_id, medicine_name, dosage, schedule, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, reminder.getUserId());
            preparedStatement.setString(2, reminder.getMedicineName());
            preparedStatement.setString(3, reminder.getDosage());
            preparedStatement.setString(4, reminder.getSchedule());
            preparedStatement.setDate(5, java.sql.Date.valueOf(reminder.getStartDate()));
            if (reminder.getEndDate() != null) {
                preparedStatement.setDate(6, java.sql.Date.valueOf(reminder.getEndDate()));
            } else {
                preparedStatement.setNull(6, java.sql.Types.DATE);
            }

            preparedStatement.executeUpdate();

            reminders.add(reminder);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get reminders for a specific user
    public List<MedicineReminder> getRemindersForUser(int userId) {
        List<MedicineReminder> userReminders = new ArrayList<>();
        String sql = "SELECT * FROM medicine_reminders WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    MedicineReminder reminder = new MedicineReminder(
                            rs.getInt("id"),
                            userId,
                            rs.getString("medicine_name"),
                            rs.getString("dosage"),
                            rs.getString("schedule"),
                            rs.getDate("start_date").toString(),
                            rs.getDate("end_date") != null ? rs.getDate("end_date").toString() : null
                    );
                    userReminders.add(reminder);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userReminders;
    }

    // Get reminders that are due for a specific user
    public List<MedicineReminder> getDueReminders(int userId) {
        List<MedicineReminder> dueReminders = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (MedicineReminder reminder : reminders) {
            if (reminder.getUserId() == userId && isDue(reminder, now)) {
                dueReminders.add(reminder);
            }
        }
        return dueReminders;
    }

    // Check if a reminder is due
    private boolean isDue(MedicineReminder reminder, LocalDateTime now) {
        LocalDate startDate = LocalDate.parse(reminder.getStartDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate endDate = reminder.getEndDate() != null ? LocalDate.parse(reminder.getEndDate(), DateTimeFormatter.ISO_LOCAL_DATE) : null;

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : null;

        return (startDateTime.isBefore(now) || startDateTime.isEqual(now)) && (endDateTime == null || endDateTime.isAfter(now));
    }

    // Delete a reminder
    public boolean deleteReminder(int reminderId) {
        String sql = "DELETE FROM medicine_reminders WHERE id = ?";

        try (Connection conn = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, reminderId);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
