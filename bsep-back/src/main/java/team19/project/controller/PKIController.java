package team19.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team19.project.dto.CertificateBasicDTO;
import team19.project.dto.CertificateDTO;
import team19.project.dto.CertificateDetailsDTO;
import team19.project.dto.IssuerDTO;
import team19.project.service.impl.PKIServiceImpl;

import java.security.cert.CertificateEncodingException;
import java.util.List;


@RestController
@RequestMapping(value = "/api/pki")
@CrossOrigin
public class PKIController {

    @Autowired
    private PKIServiceImpl pkiService;

    @GetMapping(value="/getCertificateDetails/{serialNumber}", produces = "application/json")
    public CertificateDetailsDTO getCertificateDetails(@PathVariable("serialNumber") String serialNumber) throws CertificateEncodingException {

        return pkiService.getCertificateDetails(serialNumber);
    }

    @GetMapping(value="/getAllCertificates", produces="application/json")
    public List<CertificateBasicDTO> getAllCertificates() throws CertificateEncodingException {

        return pkiService.getAllCertificates();
    }

    @GetMapping(value="/getAllCA", produces="application/json")
    public List<IssuerDTO> getAllCA() throws CertificateEncodingException {

        return pkiService.getAllCA();
    }

    @PostMapping(value="/addNewCertificate", consumes="application/json")
    public ResponseEntity<?> addNewCertificate(@RequestBody CertificateDTO certificateDTO) throws CertificateEncodingException {

        boolean certCreated = pkiService.addNewCertificate(certificateDTO);
        if(certCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    @GetMapping(value="/checkValidityStatus/{serialNumber}")
    public ResponseEntity<Boolean> checkValidityStatus(@PathVariable("serialNumber") String serialNumber){

        return new ResponseEntity<>(pkiService.checkValidityStatus(serialNumber), HttpStatus.OK);
    }




}
