package team19.project.model;

import javax.persistence.*;

@Entity
public class CertificateDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="subjectSerialNumber")
    private String subjectSerialNumber;

    @Column(name="issuerSerialNumber")
    private String issuerSerialNumber;

    @Column(name="ca")
    private boolean ca;

    @Column(name="revoked")
    private boolean revoked;

    @ManyToOne
    private RevocationReason revocationReason;

    public CertificateDB(){

    }

    public CertificateDB(String subjectSerialNumber, String issuerSerialNumber, boolean ca){
        this.subjectSerialNumber = subjectSerialNumber;
        this.issuerSerialNumber = issuerSerialNumber;
        this.ca = ca;
        this.revoked = false;
        this.revocationReason = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectSerialNumber() {
        return subjectSerialNumber;
    }

    public void setSubjectSerialNumber(String subjectSerialNumber) {
        this.subjectSerialNumber = subjectSerialNumber;
    }

    public String getIssuerSerialNumber() {
        return issuerSerialNumber;
    }

    public void setIssuerSerialNumber(String issuerSerialNumber) {
        this.issuerSerialNumber = issuerSerialNumber;
    }

    public boolean isCa() {
        return ca;
    }

    public void setCa(boolean ca) {
        this.ca = ca;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public RevocationReason getRevocationReason() {
        return revocationReason;
    }

    public void setRevocationReason(RevocationReason revocationReason) {
        this.revocationReason = revocationReason;
    }
}
