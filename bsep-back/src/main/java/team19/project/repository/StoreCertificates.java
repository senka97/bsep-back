package team19.project.repository;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import team19.project.utils.IssuerData;
import team19.project.utils.KeyStoreReader;
import team19.project.utils.KeyStoreWriter;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

@Repository
public class StoreCertificates {

    //@Value("${keystore.password}")
    //private String password;

    private KeyStoreWriter writer = new KeyStoreWriter();
    private KeyStoreReader reader = new KeyStoreReader();

    public void saveCertificate(X509Certificate[] chain, PrivateKey privateKey, String fileLocation, String password) throws CertificateEncodingException {
        String serialNumber = chain[0].getSerialNumber().toString();
        writer.loadKeyStore(fileLocation, password.toCharArray());
        writer.write(serialNumber, privateKey, serialNumber.toCharArray(), chain);
        writer.saveKeyStore(fileLocation, password.toCharArray());

        //provera
        Enumeration<String> alisases = this.getAllAliases(fileLocation, password);
        System.out.println("Svi alijasi u keystore-u" + fileLocation + ":");
        while(alisases.hasMoreElements()){
            System.out.println(alisases.nextElement());
        }
        Certificate[] certificateChain = this.reader.readCertificateChain(fileLocation,password,serialNumber);
        System.out.println("Duzina chain-a: " + certificateChain.length);
        System.out.println("Chain:");
        for(int i=0;i<certificateChain.length;i++){

            System.out.println(new JcaX509CertificateHolder((X509Certificate) certificateChain[i]).getSerialNumber());
            X500Name x500name = new JcaX509CertificateHolder((X509Certificate) certificateChain[i]).getSubject();
            RDN cn = x500name.getRDNs(BCStyle.CN)[0];
            System.out.println(IETFUtils.valueToString(cn.getFirst().getValue()));
        }
    }

    public IssuerData findIssuerBySerialNumber(String serialNumber, String fileLocation, String password) {
        return reader.readIssuerFromStore(fileLocation, serialNumber,
                password.toCharArray(), serialNumber.toCharArray());
    }

    public Certificate findCertificateBySerialNumber(String serialNumber, String fileLocation, String password) {
        return reader.readCertificate(fileLocation, password, serialNumber);
    }

    public Certificate findCertificateByAlias(String alias, String fileLocation, String password){
        return reader.readCertificate(fileLocation, password, alias);

    }

    public Certificate[] findCertificateChainBySerialNumber(String serialNumber, String fileLocation, String password) {
        return reader.readCertificateChain(fileLocation, password, serialNumber);
    }

    public Enumeration<String> getAllAliases(String keyStoreFile, String password) {
        return reader.getAllAliases(keyStoreFile, password);
    }

}
