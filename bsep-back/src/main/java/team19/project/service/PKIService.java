package team19.project.service;

import team19.project.dto.CertificateDTO;

import java.util.ArrayList;

public interface PKIService {

    ArrayList<CertificateDTO> getAllCertificates();
    void addNewCertificate(CertificateDTO certificateDTO); //mozda druga povratna vrednost
    boolean checkRevocationStatusOCSP(String serialNumber);
    void revokeCertificate(String serialNumber); //mozda druga povratna vrednost
    boolean checkValidityStatus(String serialNumber);

}
