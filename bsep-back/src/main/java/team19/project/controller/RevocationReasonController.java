package team19.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team19.project.model.RevocationReason;
import team19.project.service.impl.RevocationReasonServiceImpl;

import java.util.List;

@RestController
@RequestMapping(value = "/api/revocationReasons")
@CrossOrigin
public class RevocationReasonController {

      @Autowired
      private RevocationReasonServiceImpl revocationReasonService;

    @GetMapping(value="/getAllRevocationReasons", produces="application/json")
    public List<RevocationReason> getAllRevocationReason(){

        return this.revocationReasonService.findAll();
    }
}
