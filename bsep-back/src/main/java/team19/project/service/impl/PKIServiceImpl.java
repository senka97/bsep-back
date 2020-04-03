package team19.project.service.impl;

import org.springframework.stereotype.Service;
import team19.project.dto.CertificateDTO;
import team19.project.service.PKIService;

import java.security.*;
import java.util.ArrayList;

@Service
public class PKIServiceImpl implements PKIService {

    @Override
    public ArrayList<CertificateDTO> getAllCertificates() {

        return null;
    }

    @Override
    public void addNewCertificate(CertificateDTO certificateDTO) {

    }

    @Override
    public boolean checkRevocationStatusOCSP(String serialNumber)
    {
        return false;
    }

    @Override
    public void revokeCertificate(String serialNumber) {

    }

    @Override
    public boolean checkValidityStatus(String serialNumber) {

        return false;
    }



    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
}
