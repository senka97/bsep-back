package team19.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team19.project.dto.RevokedCertificateDTO;
import team19.project.model.RevocationReason;
import team19.project.model.RevokedCertificate;
import team19.project.repository.RevokedCertificateRepository;
import team19.project.service.RevokedCertificateService;

@Service
public class RevokedCertificateServiceImpl implements RevokedCertificateService {

    @Autowired
    private RevokedCertificateRepository revokedCertificateRepository;

    @Autowired
    private RevocationReasonServiceImpl revocationReasonService;

    @Override
    public RevokedCertificate findOne(Long id) {
        return revokedCertificateRepository.findById(id).orElse(null);
    }

    @Override
    public boolean revokeCertificate(RevokedCertificateDTO revokedCertificateDTO) {

        RevokedCertificate rc = revokedCertificateRepository.findBySerialNumber(revokedCertificateDTO.getSerialNumber());
        if(rc != null){
            return true;
        }

        RevocationReason revocationReason = revocationReasonService.findOne(revokedCertificateDTO.getIdRevocationReason());
        RevokedCertificate revokedCertificate = new RevokedCertificate(revokedCertificateDTO.getSerialNumber(),revocationReason);
        revokedCertificateRepository.save(revokedCertificate);
        return false;
    }
}
