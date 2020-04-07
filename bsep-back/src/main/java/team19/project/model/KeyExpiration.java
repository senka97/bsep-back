package team19.project.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class KeyExpiration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="serialNumber", nullable=false, unique = true)
    public String serialNumber;

    @Column(name="expirationDate", nullable=false)
    public Date expirationDate;

    public KeyExpiration() {
    }

    public KeyExpiration(String serialNumber, Date expirationDate) {
        this.serialNumber = serialNumber;
        this.expirationDate = expirationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
