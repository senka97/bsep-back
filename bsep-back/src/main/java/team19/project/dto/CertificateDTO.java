package team19.project.dto;

import team19.project.utils.CertificateType;

import java.util.ArrayList;
import java.util.List;

public class CertificateDTO {

    private String subjectCommonName;
    private String subjectFirstName;
    private String subjectLastName;
    private String subjectEmail;
    private String subjectOrganization;
    private String subjectOrganizationUnit;
    private String subjectCountry;
    private String startDate;
    private String endDate;
    private String issuerSerialNumber;
    private CertificateType certificateType;
    private List<Integer> keyUsageList;
    private List<String> extendedKeyUsageList;

    public CertificateDTO() { }

    public CertificateDTO(String subjectCommonName, String subjectFirstName,
                          String subjectLastName, String subjectEmail, String subjectOrganization,
                          String subjectOrganizationUnit, String subjectCountry, String startDate, String endDate,
                          String issuerSerialNumber, CertificateType certificateType) {
        this.subjectCommonName = subjectCommonName;
        this.subjectFirstName = subjectFirstName;
        this.subjectLastName = subjectLastName;
        this.subjectEmail = subjectEmail;
        this.subjectOrganization = subjectOrganization;
        this.subjectOrganizationUnit = subjectOrganizationUnit;
        this.subjectCountry = subjectCountry;
        this.startDate = startDate;
        this.endDate = endDate;
        this.issuerSerialNumber = issuerSerialNumber;
        this.certificateType = certificateType;
        this.keyUsageList = new ArrayList<>();
        this.extendedKeyUsageList = new ArrayList<>();
    }

    public String getSubjectCommonName() {
        return subjectCommonName;
    }

    public void setSubjectCommonName(String subjectCommonName) {
        this.subjectCommonName = subjectCommonName;
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

    public String getSubjectEmail() {
        return subjectEmail;
    }

    public void setSubjectEmail(String subjectEmail) {
        this.subjectEmail = subjectEmail;
    }

    public String getSubjectOrganization() {
        return subjectOrganization;
    }

    public void setSubjectOrganization(String subjectOrganization) {
        this.subjectOrganization = subjectOrganization;
    }

    public String getSubjectOrganizationUnit() {
        return subjectOrganizationUnit;
    }

    public void setSubjectOrganizationUnit(String subjectOrganizationUnit) {
        this.subjectOrganizationUnit = subjectOrganizationUnit;
    }

    public String getSubjectCountry() {
        return subjectCountry;
    }

    public void setSubjectCountry(String subjectCountry) {
        this.subjectCountry = subjectCountry;
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

    public String getIssuerSerialNumber() {
        return issuerSerialNumber;
    }

    public void setIssuerSerialNumber(String issuerSerialNumber) {
        this.issuerSerialNumber = issuerSerialNumber;
    }

    public CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public List<Integer> getKeyUsageList() {
        return keyUsageList;
    }

    public void setKeyUsageList(List<Integer> keyUsageList) {
        this.keyUsageList = keyUsageList;
    }

    public List<String> getExtendedKeyUsageList() {
        return extendedKeyUsageList;
    }

    public void setExtendedKeyUsageList(List<String> extendedKeyUsageList) {
        this.extendedKeyUsageList = extendedKeyUsageList;
    }
}
