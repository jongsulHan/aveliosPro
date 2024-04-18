package persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import persistence.entity.Hospital;

@Repository
public interface HospitalRepository extends CrudRepository<Hospital, Long> {
}
