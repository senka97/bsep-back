package team19.project.service.impl;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team19.project.dto.CertificateDTO;
import team19.project.model.IssuerData;
import team19.project.model.SubjectData;
import team19.project.repository.StoreCertificates;
import team19.project.service.PKIService;
import team19.project.utils.CertificateGenerator;
import team19.project.utils.CertificateType;

import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Service
public class PKIServiceImpl implements PKIService {

    @Autowired
    private CertificateGenerator certificateGenerator;
    @Autowired
    private StoreCertificates store;

    private KeyPair keyPairSubject = generateKeyPair();
    private X509Certificate cert;
    private X509Certificate issuerCertificate;
    private String fileLocation = "keystore/keystore.jks";

    @Override
    public ArrayList<CertificateDTO> getAllCertificates() {

        return null;
    }

    @Override
    public boolean addNewCertificate(CertificateDTO certificateDTO) {
        SubjectData subjectData = generateSubjectData(certificateDTO);

        if(certificateDTO.getCertificateType().equals(CertificateType.SELF_SIGNED)) {
            IssuerData issuerData = generateIssuerData(certificateDTO);
            cert = certificateGenerator.generateCertificate(subjectData, issuerData);
        } else if(certificateDTO.getCertificateType().equals(CertificateType.INTERMEDIATE)) {
            IssuerData issuerData = store.findIssuerBySerialNumber(certificateDTO.getIssuerSerialNumber(), fileLocation);
            subjectData.setSerialNumber("654321");
            issuerCertificate = (X509Certificate) store.findCertificateBySerialNumber(certificateDTO.getIssuerSerialNumber(), fileLocation);
            cert = certificateGenerator.generateCertificate(subjectData, issuerData);
        } else if(certificateDTO.getCertificateType().equals(CertificateType.END_ENTITY)) {
            return false;
        }

        if(cert == null) {
            return false;
        }

        if(issuerCertificate == null) {
            store.saveCertificate(new X509Certificate[]{cert}, keyPairSubject.getPrivate(), fileLocation);
            System.out.println("********SAVED********");
            return true;
        }

        store.saveCertificate(new X509Certificate[]{issuerCertificate, cert}, keyPairSubject.getPrivate(), fileLocation);
        System.out.println("********SAVED********");

        return true;
    }

    @Override
    public boolean checkRevocationStatusOCSP(String serialNumber)
    {
        return false;
    }

    @Override
    public void revokeCertificate(String serialNumber) {

    }

    @Override
    public boolean checkValidityStatus(String serialNumber) {

        return false;
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SubjectData generateSubjectData(CertificateDTO certificateDTO) {
        try {

            SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = iso8601Formater.parse(certificateDTO.getStartDate());
            Date endDate = iso8601Formater.parse(certificateDTO.getEndDate());

            String serialNumber = "123456";

            X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
            builder.addRDN(BCStyle.CN, certificateDTO.getSubjectCommonName());
            builder.addRDN(BCStyle.SURNAME, certificateDTO.getSubjectLastName());
            builder.addRDN(BCStyle.GIVENNAME, certificateDTO.getSubjectFirstName());
            builder.addRDN(BCStyle.O, certificateDTO.getSubjectOrganization());
            builder.addRDN(BCStyle.OU, certificateDTO.getSubjectOrganizationUnit());
            builder.addRDN(BCStyle.C, certificateDTO.getSubjectCountry());
            builder.addRDN(BCStyle.E, certificateDTO.getSubjectEmail());

            return new SubjectData(keyPairSubject.getPublic(), builder.build(), serialNumber, startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private IssuerData generateIssuerData(CertificateDTO certificateDTO) {
        try {
            X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
            builder.addRDN(BCStyle.CN, certificateDTO.getSubjectCommonName());
            builder.addRDN(BCStyle.SURNAME, certificateDTO.getSubjectLastName());
            builder.addRDN(BCStyle.GIVENNAME, certificateDTO.getSubjectFirstName());
            builder.addRDN(BCStyle.O, certificateDTO.getSubjectOrganization());
            builder.addRDN(BCStyle.OU, certificateDTO.getSubjectOrganizationUnit());
            builder.addRDN(BCStyle.C, certificateDTO.getSubjectCountry());
            builder.addRDN(BCStyle.E, certificateDTO.getSubjectEmail());

            return new IssuerData(keyPairSubject.getPrivate(), builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
