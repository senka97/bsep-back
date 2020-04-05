package team19.project.dto;

public class RevokedCertificateDTO {

    String serialNumber;
    Long idRevocationReason;

    public RevokedCertificateDTO(){

    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Long getIdRevocationReason() {
        return idRevocationReason;
    }

    public void setIdRevocationReason(Long idRevocationReason) {
        this.idRevocationReason = idRevocationReason;
    }
}
