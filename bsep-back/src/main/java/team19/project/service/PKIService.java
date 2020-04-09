package team19.project.service;

import team19.project.dto.CertificateBasicDTO;
import team19.project.dto.CertificateDTO;
import team19.project.dto.CertificateDetailsDTO;
import team19.project.dto.IssuerDTO;

import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.List;

public interface PKIService {

    ArrayList<CertificateBasicDTO> getAllCertificates() throws CertificateEncodingException;
    CertificateDetailsDTO getCertificateDetails(String serialNumber) throws CertificateEncodingException;
    boolean addNewCertificate(CertificateDTO certificateDTO) throws CertificateEncodingException; //mozda druga povratna vrednost
    boolean checkValidityStatus(String serialNumber);
    List<IssuerDTO> getAllCA() throws CertificateEncodingException;
    String getAKI(String serialNumber);

}
