package team19.project.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import team19.project.model.IssuerData;
import team19.project.utils.KeyStoreReader;
import team19.project.utils.KeyStoreWriter;

import java.security.Key;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

@Repository
public class StoreCertificates {

    private String password = "superSecretPassword";

    private KeyStoreWriter writer = new KeyStoreWriter();
    private KeyStoreReader reader = new KeyStoreReader();

    public void saveCertificate(X509Certificate[] chain, PrivateKey privateKey, String fileLocation) {
        String serialNumber = chain[0].getSerialNumber().toString();
        writer.loadKeyStore(fileLocation, password.toCharArray());
        writer.write(serialNumber, privateKey, serialNumber.toCharArray(), chain);
        writer.saveKeyStore(fileLocation, password.toCharArray());
    }

    public IssuerData findIssuerBySerialNumber(String serialNumber, String fileLocation) {
        return reader.readIssuerFromStore(fileLocation, serialNumber,
                password.toCharArray(), serialNumber.toCharArray());
    }

    public Certificate findCertificateBySerialNumber(String serialNumber, String fileLocation) {
        return reader.readCertificate(fileLocation, serialNumber, password);
    }

}
