package team19.project.dto;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CertificateBasicDTO {

    //ove ce ici neke osnovne informacije o sertifikatima kada se oni prikazuju u listi
    private String subjectFirstName;
    private String subjectLastName;
    private String startDate;
    private String endDate;

    public CertificateBasicDTO()
    {

    }

    public CertificateBasicDTO(JcaX509CertificateHolder certificateHolder){

        X500Name subject = certificateHolder.getSubject();
        RDN cn = subject.getRDNs(BCStyle.SURNAME)[0];
        this.subjectLastName = IETFUtils.valueToString(cn.getFirst().getValue());
        cn = subject.getRDNs(BCStyle.GIVENNAME)[0];
        this.subjectFirstName = IETFUtils.valueToString(cn.getFirst().getValue());
        String pattern = "dd/MMM/yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        this.startDate = df.format(certificateHolder.getNotBefore());
        this.endDate = df.format(certificateHolder.getNotAfter());

    }

    public CertificateBasicDTO(String subjectFirstName, String subjectLastName, String startDate, String endDate) {
        this.subjectFirstName = subjectFirstName;
        this.subjectLastName = subjectLastName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getSubjectFirstName() {
        return subjectFirstName;
    }

    public void setSubjectFirstName(String subjectFirstName) {
        this.subjectFirstName = subjectFirstName;
    }

    public String getSubjectLastName() {
        return subjectLastName;
    }

    public void setSubjectLastName(String subjectLastName) {
        this.subjectLastName = subjectLastName;
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
}
