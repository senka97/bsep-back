package team19.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team19.project.model.KeyExpiration;

public interface KeyExpirationRepository extends JpaRepository<KeyExpiration,Long> {

    KeyExpiration findBySerialNumber(String serialNumber);

}
