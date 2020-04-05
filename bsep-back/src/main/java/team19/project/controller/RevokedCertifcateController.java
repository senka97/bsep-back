package team19.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team19.project.dto.RevokedCertificateDTO;
import team19.project.service.impl.RevokedCertificateServiceImpl;

@RestController
@RequestMapping(value = "/api/revokedCertificates")
@CrossOrigin
public class RevokedCertifcateController {

    @Autowired
    private RevokedCertificateServiceImpl revokedCertificateService;


    @PutMapping(value="/revokeCertificate", consumes="application/json")
    public ResponseEntity<?> revokeCertificate(@RequestBody RevokedCertificateDTO revokeCertificateDTO){

        boolean alreadyRevoked = this.revokedCertificateService.revokeCertificate(revokeCertificateDTO);
        if(alreadyRevoked){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This certificate is already revoked.");

        }else {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    @GetMapping(value="/checkRevocationStatusOCSP/{serialNumber}")
    public ResponseEntity<Boolean> checkRevocationStatusOCSP(@PathVariable("serialNumber") String serialNumber){

        boolean revoked = this.revokedCertificateService.checkRevocationStatusOCSP(serialNumber);
        if(revoked) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }
}
