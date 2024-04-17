package persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import persistence.entity.Hospital;
import persistence.entity.Treatment;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
}
