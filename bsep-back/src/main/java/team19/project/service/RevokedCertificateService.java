package team19.project.service;

import team19.project.dto.RevokedCertificateDTO;
import team19.project.model.RevokedCertificate;

public interface RevokedCertificateService {

    RevokedCertificate findOne(Long id);
    boolean revokeCertificate(RevokedCertificateDTO revokedCertificateDTO);
    boolean checkRevocationStatusOCSP(String serialNumber);
}
