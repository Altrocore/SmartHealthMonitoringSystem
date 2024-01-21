import dao.DoctorPortalDao;
import dao.HealthDataDao;
import dao.UserDao;
import manager.MedicineReminderManager;
import manager.RecommendationSystem;
import model.Doctor;
import model.HealthData;
import model.MedicineReminder;
import model.User;

import java.util.Scanner;
import java.util.List;

public class HealthMonitoringApp {

    private static final UserDao userDao = new UserDao();
    private static final HealthDataDao healthDataDao = new HealthDataDao();
    private static final DoctorPortalDao doctorPortalDao = new DoctorPortalDao();
    private static final MedicineReminderManager medicineReminderManager = new MedicineReminderManager();
    private static final RecommendationSystem recommendationSystem = new RecommendationSystem();
    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser;

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to Health Monitoring System");
            System.out.println("1. Register a new user");
            System.out.println("2. Login user");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> loginUser();
                case 3 -> {
                    System.out.println("Exiting the system...");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            }

            if (currentUser != null) {
                if (currentUser.isDoctor()) {
                    displayDoctorMenu();
                } else {
                    displayPatientMenu();
                }
            }
        }
    }


    // register user
    private static void registerUser() {
        System.out.println("Register a new user");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Is the user a doctor (true/false): ");
        boolean isDoctor = scanner.nextBoolean();

        User newUser = new User(0, firstName, lastName, email, password, isDoctor);
        boolean success = userDao.createUser(newUser);

        if (success) {
            System.out.println("model.User successfully registered.");
        } else {
            System.out.println("Registration failed.");
        }
    }

    // login user
    private static void loginUser() {
        System.out.println("Login user");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userDao.getUserByEmail(email);
        if (user != null && userDao.verifyPassword(email, password)) {
            System.out.println("Login successful.");
            currentUser = user;
            if (currentUser.isDoctor()) {
                displayDoctorMenu();
            } else {
                displayPatientMenu();
            }
        } else {
            System.out.println("Login failed. Incorrect email or password.");
        }
    }

    // display doctor menu
    private static void displayDoctorMenu() {
        boolean doctorMenuActive = true;
        while (doctorMenuActive) {
            System.out.println("\nDoctor Menu");
            System.out.println("1. View Patients List");
            System.out.println("2. Assign a Patient");
            System.out.println("3. Remove a Patient");
            System.out.println("4. Access Patient Health Data");
            System.out.println("5. Send Health Recommendations");
            System.out.println("6. Prescribe Medicine");
            System.out.println("7. View Reminders for a Patient");
            System.out.println("8. View Due Reminders for a Patient");
            System.out.println("9. Delete a Medicine Reminder");
            System.out.println("10. Logout");
            System.out.print("Enter your choice: ");

            int doctorChoice = scanner.nextInt();
            scanner.nextLine();

            switch (doctorChoice) {
                case 1 -> viewPatientsList();
                case 2 -> assignPatientToDoctor();
                case 3 -> removePatientFromDoctor();
                case 4 -> accessPatientHealthData();
                case 5 -> sendHealthRecommendations();
                case 6 -> addMedicineReminder();
                case 7 -> getRemindersForUser();
                case 8 -> getDueRemindersForUser();
                case 9 -> deleteMedicineReminder();
                case 10 -> {
                    currentUser = null;
                    doctorMenuActive = false;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }


    // display patient menu
    private static void displayPatientMenu() {
        boolean patientMenuActive = true;
        while (patientMenuActive) {
            System.out.println("\nPatient Menu");
            System.out.println("1. View Health Data");
            System.out.println("2. Add Health Data");
            System.out.println("3. View Recommendations");
            System.out.println("4. View Medicine Reminders");
            System.out.println("5. View My Due Medicine Reminders");
            System.out.println("6. View Doctor Details");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");

            int patientChoice = scanner.nextInt();
            scanner.nextLine();

            switch (patientChoice) {
                case 1 -> viewHealthData();
                case 2 -> addHealthData();
                case 3 -> viewRecommendations();
                case 4 -> getRemindersForUser();
                case 5 -> getDueRemindersForUser();
                case 6 -> viewDoctorDetails();
                case 7 -> {
                    currentUser = null;
                    patientMenuActive = false;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addHealthData() {
        System.out.println("Add health data");

        int userId;
        if (currentUser.isDoctor()) {
            System.out.print("Enter patient ID: ");
            userId = scanner.nextInt();
        } else {
            userId = currentUser.getId();
        }

        System.out.print("Enter weight: ");
        double weight = scanner.nextDouble();
        System.out.print("Enter height: ");
        double height = scanner.nextDouble();
        System.out.print("Enter steps: ");
        int steps = scanner.nextInt();
        System.out.print("Enter heart rate: ");
        int heartRate = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        HealthData newHealthData = new HealthData(0, userId, weight, height, steps, heartRate, date);
        boolean success = healthDataDao.createHealthData(newHealthData);

        if (success) {
            System.out.println("Health data added successfully.");
        } else {
            System.out.println("Failed to add health data.");
        }
    }


    private static void viewRecommendations() {
        if (currentUser == null || currentUser.isDoctor()) {
            System.out.println("Access Denied: Only patients can view their health recommendations.");
            return;
        }

        System.out.println("Fetching health recommendations for you...");

        List<String> recommendations = recommendationSystem.getRecommendationsForUser(currentUser.getId());
        if (recommendations.isEmpty()) {
            System.out.println("No specific recommendations at this time.");
        } else {
            for (String recommendation : recommendations) {
                System.out.println(recommendation);
            }
        }
    }


    private static void addMedicineReminder() {
        System.out.println("Add a medicine reminder for a patient");

        System.out.print("Enter patient ID for whom the reminder is: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter medicine name: ");
        String medicineName = scanner.nextLine();
        System.out.print("Enter dosage (e.g., '2 tablets'): ");
        String dosage = scanner.nextLine();
        System.out.print("Enter schedule (e.g., 'Every 8 hours'): ");
        String schedule = scanner.nextLine();
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDate = scanner.nextLine();
        System.out.print("Enter end date (YYYY-MM-DD) or leave blank if ongoing: ");
        String endDate = scanner.nextLine();

        if (endDate.isEmpty()) {
            endDate = null;
        }

        try {
            MedicineReminder newReminder = new MedicineReminder(0, userId, medicineName, dosage, schedule, startDate, endDate);
            medicineReminderManager.addReminder(newReminder);
            System.out.println("Medicine reminder added successfully for patient ID: " + userId);
        } catch (Exception e) {
            System.out.println("Failed to add medicine reminder. Error: " + e.getMessage());
        }
    }


    private static void getRemindersForUser() {
        int userId;
        if (currentUser.isDoctor()) {
            System.out.println("Enter patient ID to view their medicine reminders: ");
            userId = scanner.nextInt();
        } else {
            userId = currentUser.getId();
        }

        List<MedicineReminder> reminders = medicineReminderManager.getRemindersForUser(userId);
        if (reminders.isEmpty()) {
            System.out.println("No medicine reminders set.");
        } else {
            for (MedicineReminder reminder : reminders) {
                System.out.println("Reminder: " + reminder.getMedicineName());
            }
        }
    }

    private static void getDueRemindersForUser() {
        int userId;
        if (currentUser.isDoctor()) {
            System.out.println("Enter patient ID to view their due medicine reminders: ");
            userId = scanner.nextInt();
        } else {
            userId = currentUser.getId();
        }

        List<MedicineReminder> dueReminders = medicineReminderManager.getDueReminders(userId);
        if (dueReminders.isEmpty()) {
            System.out.println("No due medicine reminders.");
        } else {
            for (MedicineReminder reminder : dueReminders) {
                System.out.println("Due Reminder: " + reminder.getMedicineName());
            }
        }
    }

    private static void viewPatientsList() {
        if (currentUser == null || !currentUser.isDoctor()) {
            System.out.println("Access Denied: Only doctors can view patient lists.");
            return;
        }

        System.out.println("Fetching patient list for Dr. " + currentUser.getLastName() + "...");
        try {
            List<User> patients = doctorPortalDao.getPatientsByDoctorId(currentUser.getId());
            if (patients.isEmpty()) {
                System.out.println("You currently have no patients assigned.");
            } else {
                System.out.println("List of Patients:");
                for (User patient : patients) {
                    System.out.println("ID: " + patient.getId() + ", Name: " + patient.getFirstName() + " " + patient.getLastName() + ", Email: " + patient.getEmail());
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while fetching patient list: " + e.getMessage());
        }
    }

    private static void assignPatientToDoctor() {
        System.out.println("Assign a patient to Dr. " + currentUser.getLastName());
        System.out.print("Enter patient ID: ");
        int patientId = scanner.nextInt();
        boolean success = doctorPortalDao.assignPatientToDoctor(patientId, currentUser.getId());
        if (success) {
            System.out.println("Patient assigned successfully.");
        } else {
            System.out.println("Failed to assign patient.");
        }
    }

    private static void removePatientFromDoctor() {
        System.out.println("Remove a patient from Dr. " + currentUser.getLastName());
        System.out.print("Enter patient ID: ");
        int patientId = scanner.nextInt();
        boolean success = doctorPortalDao.removePatientFromDoctor(patientId, currentUser.getId());
        if (success) {
            System.out.println("Patient removed successfully.");
        } else {
            System.out.println("Failed to remove patient.");
        }
    }

    private static void accessPatientHealthData() {
        System.out.println("Enter patient ID to view their health data: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();

        List<HealthData> healthDataList = healthDataDao.getHealthDataByUserId(patientId);
        if (healthDataList.isEmpty()) {
            System.out.println("No health data available for this patient.");
        } else {
            for (HealthData data : healthDataList) {
                System.out.println(data);
            }
        }
    }

    private static void sendHealthRecommendations() {
        System.out.println("Enter patient ID to send recommendations: ");
        int patientId = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter the recommendation: ");
        String recommendation = scanner.nextLine();

        boolean success = recommendationSystem.saveRecommendation(patientId, recommendation);
        if (success) {
            System.out.println("Recommendation sent successfully.");
        } else {
            System.out.println("Failed to send recommendation.");
        }
    }

    private static void viewHealthData() {
        List<HealthData> healthDataList = healthDataDao.getHealthDataByUserId(currentUser.getId());
        if (healthDataList.isEmpty()) {
            System.out.println("No health data available.");
        } else {
            for (HealthData data : healthDataList) {
                System.out.println(data);
            }
        }
    }

    private static void viewDoctorDetails() {
        Doctor doctor = doctorPortalDao.getDoctorByPatientId(currentUser.getId());
        if (doctor == null) {
            System.out.println("No doctor assigned to you.");
        } else {
            System.out.println("model.Doctor Details: " + doctor);
        }
    }

    private static void deleteMedicineReminder() {
        System.out.print("Enter the ID of the medicine reminder to delete: ");
        int reminderId = scanner.nextInt();

        boolean success = medicineReminderManager.deleteReminder(reminderId);
        if (success) {
            System.out.println("Medicine reminder deleted successfully.");
        } else {
            System.out.println("Failed to delete medicine reminder.");
        }
    }
}
