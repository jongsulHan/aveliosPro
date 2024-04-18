CREATE TABLE hospital (
                          hospital_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255),
                          department VARCHAR(255)
);

CREATE TABLE patient (
                         patient_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         first_name VARCHAR(255) NOT NULL,
                         last_name VARCHAR(255) NOT NULL,
                         date_of_birth DATE,
                         gender VARCHAR(255),
                         address VARCHAR(255),
                         phone_number VARCHAR(255)
);

CREATE TABLE treatment (
                           treatment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           diagnosis VARCHAR(255) NOT NULL,
                           treatmentDate DATE,
                           physicianID VARCHAR(255),
                           notes VARCHAR(255),
                           hospital_id BIGINT,
                           FOREIGN KEY (hospital_id) REFERENCES hospital(hospital_id)
);

CREATE TABLE hospital_patient (
                                  hospital_id BIGINT NOT NULL,
                                  patient_id BIGINT NOT NULL,
                                  PRIMARY KEY (hospital_id, patient_id),
                                  FOREIGN KEY (hospital_id) REFERENCES hospital(hospital_id),
                                  FOREIGN KEY (patient_id) REFERENCES patient(patient_id)
);

