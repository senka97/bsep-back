package team19.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team19.project.dto.CertificateDTO;
import team19.project.service.impl.PKIServiceImpl;

@RestController
@RequestMapping(value = "api/pki")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8081"})
public class PKIController {

    @Autowired
    private PKIServiceImpl pkiService;

    @GetMapping(value="/getAllCertificates", produces="application/json")
    public CertificateDTO[] getAllCertificate(){

        return null;
    }

    @PostMapping(value="/addNewCertificate", consumes="application/json")
    public ResponseEntity<?> addNewCertificate(@RequestBody CertificateDTO certificateDTO){

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value="/checkRevocationStatusOCSP/{serialNumber}")
    public ResponseEntity<Boolean> checkRevocationStatusOCSP(@PathVariable("serialNumber") String serialNumber){

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PutMapping(value="/revokeCertificate/{serialNumber}")
    public ResponseEntity<?> revokeCertificate(@PathVariable("serialNumber") String serialNumber){

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value="/checkValidityStatus/{serialNumber}")
    public ResponseEntity<Boolean> checkValidityStatus(@PathVariable("serialNumber") String serialNumber){

        return new ResponseEntity<>(true, HttpStatus.OK);
    }




}
