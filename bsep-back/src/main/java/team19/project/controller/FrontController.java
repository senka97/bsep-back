package team19.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontController {

    @RequestMapping({ "","/login","/certificateForm","/allCertificates", "/certificateDetails", "/newCertificate"})
    public String gui() {
        return "forward:/index.html";
    }
}
