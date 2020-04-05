package team19.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team19.project.model.RevokedCertificate;

@Repository
public interface RevokedCertificateRepository extends JpaRepository<RevokedCertificate,Long> {

        RevokedCertificate findBySerialNumber(String serialNumber);
}
