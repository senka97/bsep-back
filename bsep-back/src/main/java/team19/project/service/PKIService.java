package team19.project.service;

import org.springframework.core.io.InputStreamResource;
import team19.project.dto.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public interface PKIService {

    ArrayList<CertificateBasicDTO> getAllCertificates() throws CertificateEncodingException;
    CertificateDetailsDTO getCertificateDetails(String serialNumber) throws CertificateEncodingException, CertificateParsingException;
    boolean addNewCertificate(CertificateDTO certificateDTO) throws CertificateEncodingException; //mozda druga povratna vrednost
    boolean checkValidityStatus(String serialNumber);
    List<IssuerDTO> getAllCA() throws CertificateEncodingException;
    String getAKI(String serialNumber);
    byte[] getCertificateDownload(String serialNumber) throws CertificateEncodingException;
    boolean revokeCertificate(RevokedCertificateDTO revokedCertificateDTO);
    boolean checkRevocationStatusOCSP(String serialNumber);
}
