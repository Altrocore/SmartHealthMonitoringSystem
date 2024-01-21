package model;

public class Doctor extends User {
    private String medicalLicenseNumber;
    private String specialization;

    public Doctor(int id, String firstName, String lastName, String email, String password, boolean isDoctor, String medicalLicenseNumber, String specialization) {
        super(id, firstName, lastName, email, password, isDoctor);
        this.medicalLicenseNumber = medicalLicenseNumber;
        this.specialization = specialization;
    }

    public Doctor(int id, String firstName, String lastName, String email, String password, boolean isDoctor) {
        super(id, firstName, lastName, email, password, isDoctor);
    }

    // Getters and setters for the new properties

    public String getMedicalLicenseNumber() {
        return medicalLicenseNumber;
    }

    public void setMedicalLicenseNumber(String medicalLicenseNumber) {
        this.medicalLicenseNumber = medicalLicenseNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "model.Doctor[" +
                "name='" + getFirstName() + " " + getLastName() + '\'' +
                ", medicalLicenseNumber='" + medicalLicenseNumber + '\'' +
                ", specialization='" + specialization + '\'' +
                ']';
    }
}

