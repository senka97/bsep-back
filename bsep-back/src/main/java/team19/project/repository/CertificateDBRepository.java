package team19.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team19.project.model.CertificateDB;

import java.util.List;

public interface CertificateDBRepository extends JpaRepository<CertificateDB,Long> {

    CertificateDB findBySubjectSerialNumber(String serialNumber);
    List<CertificateDB> findAllByIssuerSerialNumber(String serialNumber);
}
