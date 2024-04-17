package persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import persistence.entity.Hospital;
import persistence.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}
