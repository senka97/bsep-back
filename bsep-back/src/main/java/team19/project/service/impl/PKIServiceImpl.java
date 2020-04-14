package team19.project.service.impl;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.util.encoders.Hex;
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
import java.security.cert.*;
import java.security.cert.Certificate;
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
    private BigIntGenerator bigIntGenerator;
    @Autowired
    private RevokedCertificateServiceImpl revokedCertificateService;
    @Autowired
    private KeyExpirationServiceImpl keyExpirationService;

    private KeyPair keyPairSubject = generateKeyPair();
    private X509Certificate cert;
    private X509Certificate issuerCertificate;
    private String fileLocation = "keystore/keystore.jks";

    @Override
    public ArrayList<CertificateBasicDTO> getAllCertificates() throws CertificateEncodingException {

        Enumeration<String> alisases = store.getAllAliases(fileLocation);
        ArrayList<CertificateBasicDTO> certificateBasicDTOS = new ArrayList<>();

        while (alisases.hasMoreElements()) {
            Certificate c = store.findCertificateByAlias(alisases.nextElement(), fileLocation);

            JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) c);

            certificateBasicDTOS.add(new CertificateBasicDTO(certHolder));

        }
        //System.out.println("Duzina liste svih sertifikata:");
       // System.out.println(certificateBasicDTOS.size());
        return certificateBasicDTOS;
    }

    @Override
    public CertificateDetailsDTO getCertificateDetails(String serialNumber) throws CertificateEncodingException, CertificateParsingException {

        X509Certificate cert = (X509Certificate) store.findCertificateBySerialNumber(serialNumber, fileLocation);

        if(cert != null)
        {
            JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) cert);
            Certificate[] chain = store.findCertificateChainBySerialNumber(serialNumber, fileLocation);
            //System.out.println("Duzina lanca "+ chain.length);
            X509Certificate x509Cert;
            Boolean isRoot;
            if(chain.length==1)
            { //ako je root onda nema nadredjenih
                 x509Cert = (X509Certificate) chain[0];
                 isRoot = true;
            }
            else
            {
                x509Cert = (X509Certificate) chain[1];
                isRoot = false;
            }

            String issuerSerialNumber = x509Cert.getSerialNumber().toString();

            CertificateDetailsDTO cddto = new CertificateDetailsDTO(certHolder,cert,issuerSerialNumber,isRoot);
            return cddto;
        }
        else return null;
    }


    @Override
    public boolean addNewCertificate(CertificateDTO certificateDTO) throws CertificateEncodingException {
        keyPairSubject = generateKeyPair();
        SubjectData subjectData = generateSubjectData(certificateDTO);

        if (certificateDTO.getCertificateType().equals(CertificateType.SELF_SIGNED)) {
            // Resetovanje issuerCertificate jer ako je pre root-a kreiran bilo koji drugi sertifikat
            // ovo polje ce imati vrednost i samim tim nece se izvrsiti linija 93 nego linija 102 i program ce puci
            issuerCertificate = null;
            IssuerData issuerData = generateIssuerData(certificateDTO);
            cert = certificateGenerator.generateCertificate(subjectData, issuerData, certificateDTO);

        } else if (certificateDTO.getCertificateType().equals(CertificateType.INTERMEDIATE)) {

            Enumeration<String> aliases = store.getAllAliases(fileLocation);
            String serialNumber = certificateDTO.getIssuerSerialNumber();
            IssuerData issuerData = store.findIssuerBySerialNumber(serialNumber, fileLocation);
            issuerCertificate = (X509Certificate) store.findCertificateBySerialNumber(serialNumber, fileLocation);
            cert = certificateGenerator.generateCertificate(subjectData, issuerData, certificateDTO);

        } else if (certificateDTO.getCertificateType().equals(CertificateType.END_ENTITY)) {

            Enumeration<String> aliases = store.getAllAliases(fileLocation);
            String serialNumber = certificateDTO.getIssuerSerialNumber();
            IssuerData issuerData = store.findIssuerBySerialNumber(serialNumber, fileLocation);
            issuerCertificate = (X509Certificate) store.findCertificateBySerialNumber(serialNumber, fileLocation);
            cert = certificateGenerator.generateCertificate(subjectData, issuerData,
                    certificateDTO);
        }

        if (cert == null) {
            return false;
        }

        //ako je self-signed
        if (issuerCertificate == null) {
            keyExpirationService.save(cert);
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

        //  zapamti kad kljuc istice za sertifikat
        keyExpirationService.save(issuerChainX509[0]);

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
                if(checkValidityStatus(((X509Certificate) c).getSerialNumber().toString())){
                    issuerDTOS.add(new IssuerDTO(certHolder));
                }
            }
        }
        return issuerDTOS;
    }

    @Override
    public String getAKI(String serialNumber) {

        X509Certificate cert = (X509Certificate) store.findCertificateBySerialNumber(serialNumber, fileLocation);
        byte[] extensionValue = cert.getExtensionValue("2.5.29.14");
        byte[] octets = DEROctetString.getInstance(extensionValue).getOctets();
        SubjectKeyIdentifier subjectKeyIdentifier = SubjectKeyIdentifier.getInstance(octets);
        byte[] keyIdentifier = subjectKeyIdentifier.getKeyIdentifier();
        String keyIdentifierHex = new String(Hex.encode(keyIdentifier));
        return keyIdentifierHex;
    }

    @Override
    public boolean checkValidityStatus(String serialNumber) {

        X509Certificate cert = (X509Certificate) store.findCertificateBySerialNumber(serialNumber, fileLocation);
        Certificate[] chain = store.findCertificateChainBySerialNumber(serialNumber, fileLocation);

        for(int i =0 ; i < chain.length; i++) {

            X509Certificate x509Cert = (X509Certificate)chain[i];
            X509Certificate x509CACert =null;


            if(i != chain.length-1) {
                x509CACert = (X509Certificate)chain[i+1];
            }else {
                x509CACert = (X509Certificate)chain[i]; //kada dodje do kraja proveri samopotpisni
            }


            //za svaki sertifikat u lancu proveri da li je istekao
            try {
                x509Cert.checkValidity();
            } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                // TODO Auto-generated catch block
                System.out.println("SERTIFIKAT: "+x509Cert.getSerialNumber()+" ISTEKAO.");
                e.printStackTrace();
                return false;
            }


            //provera potpisa
            try {
                x509Cert.verify(x509CACert.getPublicKey());
            } catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
                    | SignatureException e) {
                System.out.println("SERTIFIKAT: "+x509Cert.getSerialNumber()+" NEMA VALIDAN POTPIS.");

                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;

            }

            //provera da li se nalazi u listi povucenih
            if(revokedCertificateService.checkRevocationStatusOCSP(x509Cert.getSerialNumber().toString())) {
                System.out.println("SERTIFIKAT: "+x509Cert.getSerialNumber()+" JE POVUCEN.");
                return false;
            }

            //proveri da li je issuer CA
            if(x509CACert.getBasicConstraints() == -1) {
                System.out.println("SERTIFIKAT: "+x509CACert.getSerialNumber()+" NIJE CA.");
                return false;
            }

            //proveri kljuc?
            if(keyExpirationService.expired(x509Cert.getSerialNumber().toString())) {
                System.out.println("SERTIFIKATU: "+x509Cert.getSerialNumber()+" JE ISTEKAO KLJUC.");
                return false;
            }

        }


        System.out.println("SERTIFIKAT I LANAC SU VALIDNI.");
        return true;
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
            builder.addRDN(BCStyle.ST, certificateDTO.getSubjectState());
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
            builder.addRDN(BCStyle.ST, certificateDTO.getSubjectState());
            builder.addRDN(BCStyle.C, certificateDTO.getSubjectCountry());
            builder.addRDN(BCStyle.E, certificateDTO.getSubjectEmail());

            return new IssuerData(keyPairSubject.getPrivate(), builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
