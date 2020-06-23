package team19.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team19.project.dto.*;
import team19.project.service.impl.PKIServiceImpl;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.util.List;


@RestController
@RequestMapping(value = "/api/pki")
@CrossOrigin
public class PKIController {

    @Autowired
    private PKIServiceImpl pkiService;

    @GetMapping(value="/getCertificateDetails/{serialNumber}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CertificateDetailsDTO getCertificateDetails(@PathVariable("serialNumber") String serialNumber) throws CertificateEncodingException, CertificateParsingException {

        return pkiService.getCertificateDetails(serialNumber);
    }

    @GetMapping(value="/getAllCertificates", produces="application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<CertificateBasicDTO> getAllCertificates() throws CertificateEncodingException {

        return pkiService.getAllCertificates();
    }

    @GetMapping(value="/downloadCertificate/{serialNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> downloadCertificate(@PathVariable("serialNumber") String serialNumber) throws CertificateEncodingException, IOException {
        byte cert[] = pkiService.getCertificateDownload(serialNumber);
        return new ResponseEntity<>(cert, HttpStatus.OK);
    }


    @GetMapping(value="/getAllCA", produces="application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<IssuerDTO> getAllCA() throws CertificateEncodingException {

        return pkiService.getAllCA();
    }

    @PostMapping(value="/addNewCertificate", consumes="application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addNewCertificate(@RequestBody CertificateDTO certificateDTO) throws CertificateEncodingException {

        boolean certCreated = pkiService.addNewCertificate(certificateDTO);
        if(certCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    @GetMapping(value="/checkValidityStatus/{serialNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> checkValidityStatus(@PathVariable("serialNumber") String serialNumber){

        return new ResponseEntity<>(pkiService.checkValidityStatus(serialNumber), HttpStatus.OK);
    }

    @GetMapping(value="/getAKI/{serialNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> getAKI(@PathVariable("serialNumber") String serialNumber){

        return new ResponseEntity<>(pkiService.getAKI(serialNumber), HttpStatus.OK);
    }

    @PutMapping(value="/revokeCertificate", consumes="application/json")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> revokeCertificate(@RequestBody RevokedCertificateDTO revokeCertificateDTO){

        boolean alreadyRevoked = this.pkiService.revokeCertificate(revokeCertificateDTO);
        if(alreadyRevoked){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This certificate is already revoked.");

        }else {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    @GetMapping(value="/checkRevocationStatusOCSP/{serialNumber}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Boolean> checkRevocationStatusOCSP(@PathVariable("serialNumber") String serialNumber){

        boolean revoked = this.pkiService.checkRevocationStatusOCSP(serialNumber);
        if(revoked) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

}
