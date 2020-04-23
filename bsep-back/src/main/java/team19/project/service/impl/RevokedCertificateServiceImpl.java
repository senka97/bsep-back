package team19.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team19.project.dto.RevokedCertificateDTO;
import team19.project.model.CertificateDB;
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

    @Autowired
    private CertificateDBServiceImpl certificateDBService;

    private String fileLocationCA = "keystore/keystoreCA.jks";
    private String fileLocationEE = "keystore/keystoreEE.jks";
    private String passwordCA = "passwordCA";
    private String passwordEE = "passwordEE";

    @Override
    public RevokedCertificate findOne(Long id) {
        return revokedCertificateRepository.findById(id).orElse(null);
    }

    @Override
    public boolean revokeCertificate(RevokedCertificateDTO revokedCertificateDTO) {

        boolean revoked = this.checkRevocationStatusOCSP(revokedCertificateDTO.getSerialNumber());
        if(revoked){
            return true;
        }

        RevocationReason revocationReason = revocationReasonService.findOne(revokedCertificateDTO.getIdRevocationReason());
        RevokedCertificate revokedCertificate = new RevokedCertificate(revokedCertificateDTO.getSerialNumber(),revocationReason);
        revokedCertificateRepository.save(revokedCertificate);
        return false;

    }


    @Override
    public boolean checkRevocationStatusOCSP(String serialNumber) {

        CertificateDB certDB = certificateDBService.findCertificate(serialNumber);
        X509Certificate certificateChainX509[];

        if(certDB == null){
            return true;
        }

        if(certDB.isCa()) {
            Certificate certificateChain[] = store.findCertificateChainBySerialNumber(serialNumber, fileLocationCA, passwordCA);
            System.out.println(certificateChain.length);
            certificateChainX509 = new X509Certificate[certificateChain.length];
            for (int i = 0; i < certificateChain.length; i++) {
                certificateChainX509[i] = (X509Certificate) certificateChain[i];
            }
        }else {
            Certificate cert = store.findCertificateBySerialNumber(serialNumber,fileLocationEE,passwordEE);
            Certificate issuerChain[] = store.findCertificateChainBySerialNumber(certDB.getIssuerSerialNumber(),fileLocationCA,passwordCA);
            certificateChainX509 = new X509Certificate[issuerChain.length + 1];
            certificateChainX509[0] = (X509Certificate) cert;
            for(int i=0;i<issuerChain.length;i++){
                certificateChainX509[i+1] = (X509Certificate)issuerChain[i];
            }
        }

            //provere se svi u lancu, cim se naidje na neki koji je povucen odmah se vraca true
            for(int i=0;i<certificateChainX509.length;i++){
                System.out.println(certificateChainX509[i].getSerialNumber().toString());
                RevokedCertificate rc = revokedCertificateRepository.findBySerialNumber(certificateChainX509[i].getSerialNumber().toString());
                if(rc != null){
                    return true;
                }
            }



        /*Certificate certificateChain[] = store.findCertificateChainBySerialNumber(serialNumber,fileLocation);
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
        }*/
        return false;
    }
}
