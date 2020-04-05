package team19.project.service.impl;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team19.project.dto.CertificateBasicDTO;
import team19.project.dto.CertificateDTO;
import team19.project.dto.CertificateDetailsDTO;
import team19.project.dto.IssuerDTO;
import team19.project.utils.IssuerData;
import team19.project.utils.SubjectData;
import team19.project.repository.StoreCertificates;
import team19.project.service.PKIService;
import team19.project.utils.BigIntGenerator;
import team19.project.utils.CertificateGenerator;
import team19.project.utils.CertificateType;

import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@Service
public class PKIServiceImpl implements PKIService {

    @Autowired
    private CertificateGenerator certificateGenerator;
    @Autowired
    private StoreCertificates store;
    @Autowired
    BigIntGenerator bigIntGenerator;
    @Autowired
    private RevokedCertificateServiceImpl revokedCertificateService;

    private KeyPair keyPairSubject = generateKeyPair();
    private X509Certificate cert;
    private X509Certificate issuerCertificate;
    private String fileLocation = "keystore/keystore.jks";

    @Override
    public ArrayList<CertificateBasicDTO> getAllCertificates() {

        return null;
    }

    @Override
    public CertificateDetailsDTO getCertificateDetails(String serialNumber) {
        return null;
    }


    @Override
    public boolean addNewCertificate(CertificateDTO certificateDTO) throws CertificateEncodingException {

        SubjectData subjectData = generateSubjectData(certificateDTO);

        if (certificateDTO.getCertificateType().equals(CertificateType.SELF_SIGNED)) {

            IssuerData issuerData = generateIssuerData(certificateDTO);
            cert = certificateGenerator.generateCertificate(subjectData, issuerData, true);

        } else if (certificateDTO.getCertificateType().equals(CertificateType.INTERMEDIATE)) {

            Enumeration<String> aliases = store.getAllAliases(fileLocation);
            String serialNumber = certificateDTO.getIssuerSerialNumber();
            IssuerData issuerData = store.findIssuerBySerialNumber(serialNumber, fileLocation);
            issuerCertificate = (X509Certificate) store.findCertificateBySerialNumber(serialNumber, fileLocation);
            cert = certificateGenerator.generateCertificate(subjectData, issuerData, true);

        } else if (certificateDTO.getCertificateType().equals(CertificateType.END_ENTITY)) {

            Enumeration<String> aliases = store.getAllAliases(fileLocation);
            String serialNumber = certificateDTO.getIssuerSerialNumber();
            IssuerData issuerData = store.findIssuerBySerialNumber(serialNumber, fileLocation);
            issuerCertificate = (X509Certificate) store.findCertificateBySerialNumber(serialNumber, fileLocation);
            cert = certificateGenerator.generateCertificate(subjectData, issuerData, false);
        }

        if (cert == null) {
            return false;
        }

        //ako je self-signed
        if (issuerCertificate == null) {
            store.saveCertificate(new X509Certificate[]{cert}, keyPairSubject.getPrivate(), fileLocation);
            System.out.println("******** SAVED ROOT ********");
            return true;
        }

        //ako nije self-signed, pronadje se chain od onoga koji potpisuje kako bi se dobio ceo chain
        Certificate[] issuerChain = store.findCertificateChainBySerialNumber(certificateDTO.getIssuerSerialNumber(), fileLocation);
        X509Certificate issuerChainX509[] = new X509Certificate[issuerChain.length + 1];
        issuerChainX509[0] = cert;
        for(int i=0;i<issuerChain.length;i++){
            issuerChainX509[i+1] = (X509Certificate) issuerChain[i];
        }

       // store.saveCertificate(new X509Certificate[]{cert, issuerCertificate}, keyPairSubject.getPrivate(), fileLocation);
        store.saveCertificate(issuerChainX509, keyPairSubject.getPrivate(), fileLocation);
        System.out.println("********SAVED********");

        return true;
    }

    @Override
    public List<IssuerDTO> getAllCA() throws CertificateEncodingException {

        Enumeration<String> alisases = store.getAllAliases(fileLocation);
        List<IssuerDTO> issuerDTOS = new ArrayList<>();

        while (alisases.hasMoreElements()) {
            Certificate c = store.findCertificateByAlias(alisases.nextElement(), fileLocation);
            JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) c);
            if (((X509Certificate) c).getBasicConstraints() > -1) {
                //ovde jos provera pored toga sto je ca, da li je povucen, ili bi mozda bolje moglo da li je validan
                if(!this.revokedCertificateService.checkRevocationStatusOCSP(((X509Certificate) c).getSerialNumber().toString())){
                    issuerDTOS.add(new IssuerDTO(certHolder));
                }
            }
        }
        return issuerDTOS;
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

            String serialNumber = bigIntGenerator.generateRandom().toString();

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
