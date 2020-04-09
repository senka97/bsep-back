package team19.project.utils;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import team19.project.dto.CertificateDTO;
import team19.project.repository.StoreCertificates;

@Component
public class CertificateGenerator {

    @Autowired
    private StoreCertificates store;

    private String fileLocation = "keystore/keystore.jks";

    public CertificateGenerator() {}

    public X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData, CertificateDTO certDTO) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            builder = builder.setProvider("BC");

            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
                    new BigInteger(subjectData.getSerialNumber()),
                    subjectData.getStartDate(),
                    subjectData.getEndDate(),
                    subjectData.getX500name(),
                    subjectData.getPublicKey());

            //BasicConstraints za svaki dodajem
            certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(certDTO.isBasicConstraints()));

            //KeyUsage za svaki dodajem
            int allKeyUsages = 0;
            for (Integer i : certDTO.getKeyUsageList()) {
                allKeyUsages = allKeyUsages | i;
            }
            certGen.addExtension(Extension.keyUsage, true, new KeyUsage(allKeyUsages));

            //ExtendedKeyUsage samo ako postoji, odnosno ako je End Entity
            if(certDTO.getExtendedKeyUsageList() != null) {
                KeyPurposeId kpi[] = new KeyPurposeId[certDTO.getExtendedKeyUsageList().size()];
                int i = 0;
                for (String s : certDTO.getExtendedKeyUsageList()) {
                    ASN1ObjectIdentifier newKeyPurposeIdOID = new ASN1ObjectIdentifier(s);
                    KeyPurposeId newKeyPurposeId = KeyPurposeId.getInstance(newKeyPurposeIdOID);
                    //ExtendedKeyUsage eku = new ExtendedKeyUsage(newKeyPurposeId);
                    //certGen.addExtension(Extension.extendedKeyUsage, false, eku);
                    kpi[i] = newKeyPurposeId;
                    i++;

                }
                ExtendedKeyUsage eku = new ExtendedKeyUsage(kpi);
                certGen.addExtension(Extension.extendedKeyUsage, false, eku);
            }

            //Subject Alternative Name, samo ako postoji, nije obavezno
            if(certDTO.getValueSAN() != null){
                GeneralName altName = new GeneralName(certDTO.getTypeSAN(), certDTO.getValueSAN());
                GeneralNames subjectAltName = new GeneralNames(altName);
                certGen.addExtension(Extension.subjectAlternativeName,false,subjectAltName);
            }

            //Za sve se dodaje Subject Key Identifier
            JcaX509ExtensionUtils utils = new JcaX509ExtensionUtils();

            SubjectKeyIdentifier ski = utils.createSubjectKeyIdentifier(subjectData.getPublicKey());
            certGen.addExtension(Extension.subjectKeyIdentifier, false, ski);

            //Ako nije root doda se i Authority Key Identifier
            if(certDTO.getCertificateType() != CertificateType.SELF_SIGNED) {
                java.security.cert.Certificate certIssuer =  store.findCertificateBySerialNumber(certDTO.getIssuerSerialNumber(), fileLocation);
                AuthorityKeyIdentifier aki = utils.createAuthorityKeyIdentifier(certIssuer.getPublicKey());
                certGen.addExtension(Extension.authorityKeyIdentifier, false, aki);
            }


            X509CertificateHolder certHolder = certGen.build(contentSigner);
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");
            return certConverter.getCertificate(certHolder);

        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (CertIOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
