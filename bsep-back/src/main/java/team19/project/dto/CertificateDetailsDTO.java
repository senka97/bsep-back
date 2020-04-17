package team19.project.dto;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.util.encoders.Hex;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CertificateDetailsDTO {

    private String serialNumber;
    private int version;
    private String signatureAlgorithm;
    private String signatureHashAlgorithm;
    private String publicKey;
    private String startDate;
    private String endDate;
    private Boolean isRoot;

    private String subject;
    private String subjectGivenname;
    private String subjectSurname;
    private String subjectEmail;
    private String subjectCommonName;

    private String issuer;
    private String issuerGivenname;
    private String issuerSurname;
    private String issuerEmail;
    private String issuerCommonName;
    private String issuerSerialNumber;

    private String type;
    private List<String> subjectAlternativeNames;
    private String authorityKeyIdentifier;
    private String subjectKeyIdentifier;
    private List<String> keyUsageList;
    private List<String> extendedKeyUsageList;

    public CertificateDetailsDTO()
    {

    }

    public CertificateDetailsDTO(JcaX509CertificateHolder certificateHolder, X509Certificate cert, String issuerSerialNumber, Boolean isRoot) throws CertificateParsingException {
        this.serialNumber = certificateHolder.getSerialNumber().toString();
        this.version =certificateHolder.getVersionNumber();
        //this.signatureAlgorithm = certificateHolder.getSignatureAlgorithm().toString();
        this.signatureAlgorithm = "sha256RSA";
        this.signatureHashAlgorithm = "sha256";
        this.publicKey = cert.getPublicKey().toString();
        this.isRoot = isRoot;

        String pattern = "dd/MMM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        this.startDate = df.format(certificateHolder.getNotBefore());
        this.endDate = df.format(certificateHolder.getNotAfter());

        X500Name subject = certificateHolder.getSubject();
        generateSubject(subject);

        X500Name issuer = certificateHolder.getIssuer();
        generateIssuer(issuer);
        this.issuerSerialNumber = issuerSerialNumber;

        if(cert.getBasicConstraints()>-1)
        {
            this.type = "CA";
        }
        else
        {
            this.type = "END-ENTITY";
        }

        //subject key identifier
        SubjectKeyIdentifier subjectKeyIdent = SubjectKeyIdentifier.getInstance(DEROctetString.getInstance(cert.getExtensionValue("2.5.29.14")).getOctets());
        byte[] keyIdentifier = subjectKeyIdent.getKeyIdentifier();
        this.subjectKeyIdentifier = new String(Hex.encode(keyIdentifier));

        //authority key identifier
        if(!isRoot){
            AuthorityKeyIdentifier authorityKeyIdent = AuthorityKeyIdentifier.getInstance(DEROctetString.getInstance(cert.getExtensionValue("2.5.29.35")).getOctets());
            keyIdentifier = authorityKeyIdent.getKeyIdentifier();
            this.authorityKeyIdentifier = new String(Hex.encode(keyIdentifier));
        }

        //Extended key usage
        if(cert.getExtendedKeyUsage() != null)
        {
            this.extendedKeyUsageList = new ArrayList<>();
            for(String s :cert.getExtendedKeyUsage())
            {
                System.out.println("Extended key usage");
                System.out.println(s);
                switch (s){
                    case "1.3.6.5.5.7.3.3": this.extendedKeyUsageList.add("Code signing"); break;
                    case "1.3.6.5.5.7.3.1": this.extendedKeyUsageList.add("Server Authentication"); break;
                    case "1.3.6.5.5.7.3.2": this.extendedKeyUsageList.add("Client Authentication"); break;
                    case "1.3.6.5.5.7.3.9": this.extendedKeyUsageList.add("OCSP signing"); break;
                }

            }
        }

        //subject alternative names
        if(cert.getSubjectAlternativeNames() != null) {
            this.subjectAlternativeNames = new ArrayList<String>();
            for (List<?> s : cert.getSubjectAlternativeNames()) {
                String[] split = s.toString().replace("]", "").replace("[", "").split(",");
                System.out.println(split[0] + " : " + split[1]);
                Integer code = Integer.parseInt(split[0]);
                switch (code) {
                    case 2:
                        this.subjectAlternativeNames.add("DNS Name = " + split[1]);
                        break;
                    case 6:
                        this.subjectAlternativeNames.add("URI = " + split[1]);
                        break;
                    case 7:
                        this.subjectAlternativeNames.add("IP Adress = " + split[1]);
                        break;
                }
            }
        }

        //key usage
        this.keyUsageList = new ArrayList<String>();
        if(cert.getKeyUsage()[0]){
            keyUsageList.add("Digital Signature");
        }

        if(cert.getKeyUsage()[1]){
            keyUsageList.add("Non Repudiation");
        }

        if(cert.getKeyUsage()[2]){
            keyUsageList.add("Key Encipherment");
        }

        if(cert.getKeyUsage()[3]){
            keyUsageList.add("Data Encipherment");
        }

        if(cert.getKeyUsage()[4]){
            keyUsageList.add("Key Agreement");
        }

        if(cert.getKeyUsage()[5]){
            keyUsageList.add("KeyCert Sign");
        }

        if(cert.getKeyUsage()[6]){
            keyUsageList.add("CRL Sign");
        }

        if(cert.getKeyUsage()[7]){
            keyUsageList.add("Encipher Only");
        }

        if(cert.getKeyUsage()[8]){
            keyUsageList.add("Decipher Only");
        }


    }

    private void generateSubject(X500Name subject)
    {
        RDN cn;
        if(subject.getRDNs(BCStyle.E).length > 0) {
            cn = subject.getRDNs(BCStyle.E)[0];
            this.subjectEmail = IETFUtils.valueToString(cn.getFirst().getValue());
        }

        String temp;
        if(subject.getRDNs(BCStyle.CN).length > 0) {
            cn = subject.getRDNs(BCStyle.CN)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.subjectCommonName = temp;
            this.subject = "CN=" + temp;
        }
        if(subject.getRDNs(BCStyle.O).length > 0) {
            cn = subject.getRDNs(BCStyle.O)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.subject = this.subject + "; O=" + temp;

        }
        if(subject.getRDNs(BCStyle.OU).length > 0) {
            cn = subject.getRDNs(BCStyle.OU)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.subject = this.subject + "; OU=" + temp;
        }
        if(subject.getRDNs(BCStyle.ST).length > 0) {
            cn = subject.getRDNs(BCStyle.ST)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.subject = this.subject + "; ST=" + temp;
        }
        if(subject.getRDNs(BCStyle.C).length > 0) {
            cn = subject.getRDNs(BCStyle.C)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.subject = this.subject + "; C=" + temp;
        }
        if(subject.getRDNs(BCStyle.GIVENNAME).length > 0) {
            cn = subject.getRDNs(BCStyle.GIVENNAME)[0];
            this.subjectGivenname = IETFUtils.valueToString(cn.getFirst().getValue());
        }
        if(subject.getRDNs(BCStyle.SURNAME).length > 0) {
            cn = subject.getRDNs(BCStyle.SURNAME)[0];
            this.subjectSurname = IETFUtils.valueToString(cn.getFirst().getValue());
        }

    }

    private void generateIssuer(X500Name issuer)
    {
        RDN cn;
        if(issuer.getRDNs(BCStyle.E).length > 0) {
            cn = issuer.getRDNs(BCStyle.E)[0];
            this.issuerEmail = IETFUtils.valueToString(cn.getFirst().getValue());
        }

        String temp;
        if(issuer.getRDNs(BCStyle.CN).length > 0) {
            cn = issuer.getRDNs(BCStyle.CN)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.issuerCommonName = temp;
            this.issuer = "CN=" + temp;
        }
        if(issuer.getRDNs(BCStyle.O).length > 0) {
            cn = issuer.getRDNs(BCStyle.O)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.issuer = this.issuer + "; O=" + temp;
        }
        if(issuer.getRDNs(BCStyle.OU).length > 0) {
            cn = issuer.getRDNs(BCStyle.OU)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.issuer = this.issuer + "; OU=" + temp;
        }
        if(issuer.getRDNs(BCStyle.ST).length > 0) {
            cn = issuer.getRDNs(BCStyle.ST)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.issuer = this.issuer + "; ST=" + temp;
        }
        if(issuer.getRDNs(BCStyle.C).length > 0) {
            cn = issuer.getRDNs(BCStyle.C)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.issuer = this.issuer + "; C=" + temp;
        }
        if(issuer.getRDNs(BCStyle.GIVENNAME).length > 0) {
            cn = issuer.getRDNs(BCStyle.GIVENNAME)[0];
            this.issuerGivenname = IETFUtils.valueToString(cn.getFirst().getValue());
        }
        if(issuer.getRDNs(BCStyle.SURNAME).length > 0) {
            cn = issuer.getRDNs(BCStyle.SURNAME)[0];
            this.issuerSurname = IETFUtils.valueToString(cn.getFirst().getValue());
        }
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubjectEmail() {
        return subjectEmail;
    }

    public void setSubjectEmail(String subjectEmail) {
        this.subjectEmail = subjectEmail;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIssuerEmail() {
        return issuerEmail;
    }

    public void setIssuerEmail(String issuerEmail) {
        this.issuerEmail = issuerEmail;
    }

    public String getIssuerSerialNumber() {
        return issuerSerialNumber;
    }

    public void setIssuerSerialNumber(String issuerSerialNumber) {
        this.issuerSerialNumber = issuerSerialNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubjectCommonName() {
        return subjectCommonName;
    }

    public void setSubjectCommonName(String subjectCommonName) {
        this.subjectCommonName = subjectCommonName;
    }

    public String getIssuerCommonName() {
        return issuerCommonName;
    }

    public void setIssuerCommonName(String issuerCommonName) {
        this.issuerCommonName = issuerCommonName;
    }

    public String getSignatureHashAlgorithm() {
        return signatureHashAlgorithm;
    }

    public void setSignatureHashAlgorithm(String signatureHashAlgorithm) {
        this.signatureHashAlgorithm = signatureHashAlgorithm;
    }


    public List<String> getSubjectAlternativeNames() {
        return subjectAlternativeNames;
    }

    public void setSubjectAlternativeNames(List<String> subjectAlternativeNames) {
        this.subjectAlternativeNames = subjectAlternativeNames;
    }

    public String getSubjectGivenname() {
        return subjectGivenname;
    }

    public void setSubjectGivenname(String subjectGivenname) {
        this.subjectGivenname = subjectGivenname;
    }

    public String getSubjectSurname() {
        return subjectSurname;
    }

    public void setSubjectSurname(String subjectSurname) {
        this.subjectSurname = subjectSurname;
    }

    public String getIssuerGivenname() {
        return issuerGivenname;
    }

    public void setIssuerGivenname(String issuerGivenname) {
        this.issuerGivenname = issuerGivenname;
    }

    public String getIssuerSurname() {
        return issuerSurname;
    }

    public void setIssuerSurname(String issuerSurname) {
        this.issuerSurname = issuerSurname;
    }

    public String getAuthorityKeyIdentifier() {
        return authorityKeyIdentifier;
    }

    public void setAuthorityKeyIdentifier(String authorityKeyIdentifier) {
        this.authorityKeyIdentifier = authorityKeyIdentifier;
    }

    public String getSubjectKeyIdentifier() {
        return subjectKeyIdentifier;
    }

    public void setSubjectKeyIdentifier(String subjectKeyIdentifier) {
        this.subjectKeyIdentifier = subjectKeyIdentifier;
    }

    public Boolean getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(Boolean root) {
        isRoot = root;
    }

    public List<String> getKeyUsageList() {
        return keyUsageList;
    }

    public void setKeyUsageList(List<String> keyUsageList) {
        this.keyUsageList = keyUsageList;
    }

    public List<String> getExtendedKeyUsageList() {
        return extendedKeyUsageList;
    }

    public void setExtendedKeyUsageList(List<String> extendedKeyUsageList) {
        this.extendedKeyUsageList = extendedKeyUsageList;
    }


}
