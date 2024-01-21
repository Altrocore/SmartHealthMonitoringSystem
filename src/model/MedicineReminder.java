package model;

public class MedicineReminder {
    private int id;
    private int userId;
    private String medicineName;
    private String dosage;
    private String schedule;
    private String startDate;
    private String endDate;

    public MedicineReminder(int id, int userId, String medicineName, String dosage, String schedule, String startDate, String endDate) {
        this.id = id;
        this.userId = userId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.schedule = schedule;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
