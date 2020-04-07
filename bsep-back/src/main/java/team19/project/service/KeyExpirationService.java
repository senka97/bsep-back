package team19.project.service;

import team19.project.model.KeyExpiration;

import java.security.cert.X509Certificate;

public interface KeyExpirationService {

    boolean expired(String serialNumber);
    KeyExpiration save(X509Certificate certificate);

    }
