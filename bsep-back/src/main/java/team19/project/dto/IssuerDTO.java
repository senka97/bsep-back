package team19.project.dto;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

public class IssuerDTO {

    private String serialNumber;
    private String commonName;
    private String surname;
    private String givenName;
    private String org;
    private String orgUnit;
    private String country;
    private String email;

    public IssuerDTO(){}

    public IssuerDTO(JcaX509CertificateHolder issuerHolder){
        this.serialNumber = issuerHolder.getSerialNumber().toString();
        X500Name subject = issuerHolder.getSubject();
        RDN cn;
        if(subject.getRDNs(BCStyle.CN).length > 0) {
            cn = subject.getRDNs(BCStyle.CN)[0];
            this.commonName = IETFUtils.valueToString(cn.getFirst().getValue());
        }
        if(subject.getRDNs(BCStyle.SURNAME).length > 0) {
            cn = subject.getRDNs(BCStyle.SURNAME)[0];
            this.surname = IETFUtils.valueToString(cn.getFirst().getValue());
        }
        if(subject.getRDNs(BCStyle.GIVENNAME).length > 0) {
            cn = subject.getRDNs(BCStyle.GIVENNAME)[0];
            this.givenName = IETFUtils.valueToString(cn.getFirst().getValue());
        }
        if(subject.getRDNs(BCStyle.O).length > 0) {
            cn = subject.getRDNs(BCStyle.O)[0];
            this.org = IETFUtils.valueToString(cn.getFirst().getValue());
        }
        if(subject.getRDNs(BCStyle.OU).length > 0) {
            cn = subject.getRDNs(BCStyle.OU)[0];
            this.orgUnit = IETFUtils.valueToString(cn.getFirst().getValue());
        }
        if(subject.getRDNs(BCStyle.C).length > 0) {
            cn = subject.getRDNs(BCStyle.C)[0];
            this.country = IETFUtils.valueToString(cn.getFirst().getValue());
        }
        if(subject.getRDNs(BCStyle.E).length > 0) {
            cn = subject.getRDNs(BCStyle.E)[0];
            this.email = IETFUtils.valueToString(cn.getFirst().getValue());
        }
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getOrgUnit() {
        return orgUnit;
    }

    public void setOrgUnit(String orgUnit) {
        this.orgUnit = orgUnit;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
