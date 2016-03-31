package org.example.ws.web;

import java.security.Principal;
import java.util.Date;

import org.example.ws.model.Account;
import org.example.ws.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(
            value = "/login",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse login(ModelMap model, Principal principal ) {
    	
    	String name = principal.getName();
    	Account account = accountRepository.findByUsername(name);
    	
        return new LoginResponse(Jwts.builder().setSubject(account.getUsername())
            .claim("roles", account.getRoles()).setIssuedAt(new Date())
            .signWith(SignatureAlgorithm.HS256, "secretkey").compact());
        
    }

    @SuppressWarnings("unused")
    private static class LoginResponse {
        public String token;

        public LoginResponse(final String token) {
            this.token = token;
        }
    }
}
