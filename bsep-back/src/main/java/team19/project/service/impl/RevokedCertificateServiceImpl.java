package team19.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team19.project.dto.RevokedCertificateDTO;
import team19.project.model.RevocationReason;
import team19.project.model.RevokedCertificate;
import team19.project.repository.RevokedCertificateRepository;
import team19.project.repository.StoreCertificates;
import team19.project.service.RevokedCertificateService;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

@Service
public class RevokedCertificateServiceImpl implements RevokedCertificateService {

    @Autowired
    private RevokedCertificateRepository revokedCertificateRepository;

    @Autowired
    private RevocationReasonServiceImpl revocationReasonService;

    @Autowired
    private StoreCertificates store;

    private String fileLocation = "keystore/keystore.jks";

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

    @Override
    public boolean checkRevocationStatusOCSP(String serialNumber) {

        Certificate certificateChain[] = store.findCertificateChainBySerialNumber(serialNumber,fileLocation);
        System.out.println(certificateChain.length);
        X509Certificate certificateChainX509[] = new X509Certificate[certificateChain.length];
        for(int i=0;i<certificateChain.length;i++){
            certificateChainX509[i] = (X509Certificate) certificateChain[i];
        }

        //provere se svi u lancu, cim se naidje na neki koji je povucen odmah se vraca true
        for(int i=0;i<certificateChainX509.length;i++){
            System.out.println(certificateChainX509[i].getSerialNumber().toString());
            RevokedCertificate rc = revokedCertificateRepository.findBySerialNumber(certificateChainX509[i].getSerialNumber().toString());
            if(rc != null){
                return true;
            }
        }
        return false;
    }
}
