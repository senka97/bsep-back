package team19.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team19.project.model.KeyExpiration;
import team19.project.repository.KeyExpirationRepository;
import team19.project.service.KeyExpirationService;

import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
@Service
public class KeyExpirationServiceImpl implements KeyExpirationService {

    @Autowired
    private KeyExpirationRepository keyExpirationRepository;

    @Override
    public boolean expired(String serialNumber){
        KeyExpiration keyExpiration = keyExpirationRepository.findBySerialNumber(serialNumber);

        if(keyExpiration == null){
            return true;
        }

        Date date = new Date(System.currentTimeMillis());

        if(keyExpiration.getExpirationDate().before(date))
            return true;

        return false;
    }

    @Override
    public KeyExpiration save(X509Certificate certificate){
        Date date = certificate.getNotBefore();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, 2);
        date = c.getTime();
        return keyExpirationRepository.save(new KeyExpiration(certificate.getSerialNumber().toString(),date));
    }
}
