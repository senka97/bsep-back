package team19.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team19.project.model.CertificateDB;
import team19.project.repository.CertificateDBRepository;
import team19.project.service.CertificateDBService;

import java.util.List;

@Service
public class CertificateDBServiceImpl implements CertificateDBService {

    @Autowired
    private CertificateDBRepository certificateDBRepository;

    @Override
    public CertificateDB findCertificate(String serialNumber) {
        return certificateDBRepository.findBySubjectSerialNumber(serialNumber);
    }

    @Override
    public void save(CertificateDB certDB) {
        this.certificateDBRepository.save(certDB);
    }

    @Override
    public List<CertificateDB> findAllFirstChildren(String serialNumber) {
        return certificateDBRepository.findAllByIssuerSerialNumber(serialNumber);
    }


}
