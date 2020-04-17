package team19.project.dto;

import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CertificateBasicDTO {

    private String serialNumber;
    private String startDate;
    private String endDate;
    private String subject;

    public CertificateBasicDTO()
    {

    }

    public CertificateBasicDTO(JcaX509CertificateHolder certificateHolder){

        X500Name subject = certificateHolder.getSubject();
        String temp;
        RDN cn;
        if(subject.getRDNs(BCStyle.CN).length > 0) {
            cn = subject.getRDNs(BCStyle.CN)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
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
        if(subject.getRDNs(BCStyle.C).length > 0) {
            cn = subject.getRDNs(BCStyle.C)[0];
            temp = IETFUtils.valueToString(cn.getFirst().getValue());
            this.subject = this.subject + "; C=" + temp;
        }

        String pattern = "dd/MMM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        this.startDate = df.format(certificateHolder.getNotBefore());
        this.endDate = df.format(certificateHolder.getNotAfter());
        this.serialNumber=certificateHolder.getSerialNumber().toString();

       /* byte[] help;
        Extension ext = certificateHolder.getExtension(Extension.basicConstraints);
        if (ext != null)
        {
            try
            {
                help = ext.getExtnValue().getEncoded(ASN1Encoding.DER);
                System.out.println(help);
                this.type=help.toString();
            }
            catch (Exception e)
            {
                throw new RuntimeException("error encoding " + e.toString());
            }
        }*/

    }

    public CertificateBasicDTO(String subject, String startDate, String endDate,String serialNumber,String type) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.serialNumber = serialNumber;
        this.subject = subject;
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
