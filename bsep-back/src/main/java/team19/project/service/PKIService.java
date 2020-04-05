package team19.project.service;

import team19.project.dto.CertificateBasicDTO;
import team19.project.dto.CertificateDTO;
import team19.project.dto.CertificateDetailsDTO;
import team19.project.dto.IssuerDTO;

import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.List;

public interface PKIService {

    ArrayList<CertificateBasicDTO> getAllCertificates();
    CertificateDetailsDTO getCertificateDetails(String serialNumber);
    boolean addNewCertificate(CertificateDTO certificateDTO) throws CertificateEncodingException; //mozda druga povratna vrednost
    boolean checkRevocationStatusOCSP(String serialNumber);
    void revokeCertificate(String serialNumber); //mozda druga povratna vrednost
    boolean checkValidityStatus(String serialNumber);
    List<IssuerDTO> getAllCA() throws CertificateEncodingException;

}
