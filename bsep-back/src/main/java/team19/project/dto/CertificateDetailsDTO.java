package team19.project.dto;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
    private String[] subjectAlternativeNames;
    private String authorityKeyIdentifier;
    private String subjectKeyIdentifier;
    // key usage
    // extended key usage

    public CertificateDetailsDTO()
    {

    }

    public CertificateDetailsDTO(JcaX509CertificateHolder certificateHolder, X509Certificate cert, String issuerSerialNumber, Boolean isRoot)
    {
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

    }

    private void generateSubject(X500Name subject)
    {
        RDN cn = subject.getRDNs(BCStyle.E)[0];
        this.subjectEmail = IETFUtils.valueToString(cn.getFirst().getValue());

        String temp;
        cn = subject.getRDNs(BCStyle.CN)[0];
        temp = IETFUtils.valueToString(cn.getFirst().getValue());
        this.subjectCommonName = temp;
        if(temp.length() != 0)
        {
            this.subject =  "CN=" + temp;
        }
        cn = subject.getRDNs(BCStyle.O)[0];
        temp = IETFUtils.valueToString(cn.getFirst().getValue());
        if(temp.length()!=0)
        {
            this.subject = this.subject + "; 0=" + temp;
        }
        cn = subject.getRDNs(BCStyle.OU)[0];
        temp = IETFUtils.valueToString(cn.getFirst().getValue());
        if(temp.length()!=0)
        {
            this.subject = this.subject + "; OU=" + temp;
        }
        cn = subject.getRDNs(BCStyle.C)[0];
        temp = IETFUtils.valueToString(cn.getFirst().getValue());
        if(temp.length()!=0)
        {
            this.subject = this.subject + "; C=" + temp;
        }

        cn = subject.getRDNs(BCStyle.GIVENNAME)[0];
        this.subjectGivenname = IETFUtils.valueToString(cn.getFirst().getValue());
        cn = subject.getRDNs(BCStyle.SURNAME)[0];
        this.subjectSurname= IETFUtils.valueToString(cn.getFirst().getValue());

    }

    private void generateIssuer(X500Name issuer)
    {
        RDN cn = issuer.getRDNs(BCStyle.E)[0];
        this.issuerEmail = IETFUtils.valueToString(cn.getFirst().getValue());

        String temp;
        cn = issuer.getRDNs(BCStyle.CN)[0];
        temp = IETFUtils.valueToString(cn.getFirst().getValue());
        this.issuerCommonName = temp;
        if(temp.length() != 0)
        {
            this.issuer =  "CN=" + temp;
        }
        cn = issuer.getRDNs(BCStyle.O)[0];
        temp = IETFUtils.valueToString(cn.getFirst().getValue());
        if(temp.length()!=0)
        {
            this.issuer = this.issuer + "; 0=" + temp;
        }
        cn = issuer.getRDNs(BCStyle.OU)[0];
        temp = IETFUtils.valueToString(cn.getFirst().getValue());
        if(temp.length()!=0)
        {
            this.issuer = this.issuer + "; OU=" + temp;
        }
        cn = issuer.getRDNs(BCStyle.C)[0];
        temp = IETFUtils.valueToString(cn.getFirst().getValue());
        if(temp.length()!=0)
        {
            this.issuer = this.issuer + "; C=" + temp;
        }

        cn = issuer.getRDNs(BCStyle.GIVENNAME)[0];
        this.issuerGivenname = IETFUtils.valueToString(cn.getFirst().getValue());
        cn = issuer.getRDNs(BCStyle.SURNAME)[0];
        this.issuerSurname= IETFUtils.valueToString(cn.getFirst().getValue());
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


    public String[] getSubjectAlternativeNames() {
        return subjectAlternativeNames;
    }

    public void setSubjectAlternativeNames(String[] subjectAlternativeNames) {
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
}
