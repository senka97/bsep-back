package team19.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import team19.project.dto.UserTokenState;
import team19.project.model.Admin;
import team19.project.security.TokenUtils;
import team19.project.security.auth.JwtAuthenticationRequest;
import team19.project.service.impl.CustomUserDetailsService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class AuthenticationController {

    @Autowired
    TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest,
                                                       HttpServletResponse response) throws AuthenticationException, IOException {

        if(!checkMail(authenticationRequest.getEmail())) {
            System.out.println("Invalid email");
            return new ResponseEntity<>("Invalid email", HttpStatus.BAD_REQUEST);
        }

        Admin admin = (Admin)userService.loadUserByUsername(authenticationRequest.getEmail());

        if(admin != null)
        {
            if(org.springframework.security.crypto.bcrypt.BCrypt.checkpw(authenticationRequest.getPassword(), admin.getPassword())){
                System.out.println("Logged in successfully, email: " + admin.getUsername());
            }else{
                System.out.println("Invalid password");
                return new ResponseEntity<>("Invalid password", HttpStatus.BAD_REQUEST);
            }

            final Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Admin user = (Admin) authentication.getPrincipal();
            String jwt = tokenUtils.generateToken(user.getUsername());
            int expiresIn = tokenUtils.getExpiredIn();

            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
        }
        else
        {
            return new ResponseEntity<>("User doesn't exist in the system", HttpStatus.NOT_FOUND);
        }


    }

    public boolean checkMail(String mail) {

        if(mail.isEmpty()) {
            return false;
        }

        if(!Pattern.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", mail)){
            return false;
        }

        return true;
    }

}
