package team19.project.service;

import team19.project.model.CertificateDB;

import java.util.List;

public interface CertificateDBService {

    CertificateDB findCertificate(String serialNumber);
    void save(CertificateDB certDB);
    List<CertificateDB> findAllFirstChildren(String serialNumber);
}
