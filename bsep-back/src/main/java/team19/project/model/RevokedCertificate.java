package team19.project.model;

import javax.persistence.*;

@Entity
public class RevokedCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="serialNumber", nullable=false, unique = true)
    private String serialNumber;

    @ManyToOne
    private RevocationReason revocationReason;

    public RevokedCertificate(){

    }

    public RevokedCertificate(String serialNumber, RevocationReason revocationReason){
        this.serialNumber = serialNumber;
        this.revocationReason = revocationReason;
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

    public RevocationReason getRevocationReason() {
        return revocationReason;
    }

    public void setRevocationReason(RevocationReason revocationReason) {
        this.revocationReason = revocationReason;
    }
}
