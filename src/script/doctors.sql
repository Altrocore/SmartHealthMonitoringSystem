CREATE TABLE doctors (
    user_id INT PRIMARY KEY,
    specialization VARCHAR(255),
    medicalLicenseNumber VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
