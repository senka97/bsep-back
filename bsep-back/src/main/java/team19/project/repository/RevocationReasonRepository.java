package team19.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team19.project.model.RevocationReason;

@Repository
public interface RevocationReasonRepository extends JpaRepository<RevocationReason,Long> {


}
