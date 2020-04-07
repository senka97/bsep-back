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
    private String publicKey;
    private String startDate;
    private String endDate;

    private String subject;
    private String subjectName;
    private String subjectEmail;

    private String issuer;
    private String issuerName;
    private String issuerEmail;


    public CertificateDetailsDTO(JcaX509CertificateHolder certificateHolder, X509Certificate cert)
    {
        this.serialNumber = certificateHolder.getSerialNumber().toString();
        this.version = certificateHolder.getVersionNumber();
        this.signatureAlgorithm = certificateHolder.getSignatureAlgorithm().toString();
        this.publicKey = cert.getPublicKey().toString();

        String pattern = "dd/MMM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        this.startDate = df.format(certificateHolder.getNotBefore());
        this.endDate = df.format(certificateHolder.getNotAfter());

        X500Name subject = certificateHolder.getSubject();
        generateSubject(subject);

        X500Name issuer = certificateHolder.getIssuer();
        generateIssuer(issuer);

    }

    private void generateSubject(X500Name subject)
    {
        RDN cn = subject.getRDNs(BCStyle.E)[0];
        this.subjectEmail = IETFUtils.valueToString(cn.getFirst().getValue());

        String temp;
        cn = subject.getRDNs(BCStyle.CN)[0];
        temp = IETFUtils.valueToString(cn.getFirst().getValue());
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

        String name;
        cn = subject.getRDNs(BCStyle.GIVENNAME)[0];
        name = IETFUtils.valueToString(cn.getFirst().getValue());
        if(name.length()!=0)
        {
            this.subjectName = name;
        }
        cn = subject.getRDNs(BCStyle.SURNAME)[0];
        name = IETFUtils.valueToString(cn.getFirst().getValue());
        if(name.length()!=0)
        {
            this.subjectName = this.subjectName + " " + name;
        }
    }

    private void generateIssuer(X500Name issuer)
    {
        RDN cn = issuer.getRDNs(BCStyle.E)[0];
        this.issuerEmail = IETFUtils.valueToString(cn.getFirst().getValue());

        String temp;
        cn = issuer.getRDNs(BCStyle.CN)[0];
        temp = IETFUtils.valueToString(cn.getFirst().getValue());
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

        String name;
        cn = issuer.getRDNs(BCStyle.GIVENNAME)[0];
        name = IETFUtils.valueToString(cn.getFirst().getValue());
        if(name.length()!=0)
        {
            this.issuerName = name;
        }
        cn = issuer.getRDNs(BCStyle.SURNAME)[0];
        name = IETFUtils.valueToString(cn.getFirst().getValue());
        if(name.length()!=0)
        {
            this.issuerName = this.issuerName + " " + name;
        }
    }
}
