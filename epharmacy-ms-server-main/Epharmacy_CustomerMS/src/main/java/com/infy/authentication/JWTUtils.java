package com.infy.authentication;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.infy.entity.Customer;
import com.infy.repository.CustomerRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTUtils {
    private final SecretKey secretKey=Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private long expertiationTime=86400000;
    private final  CustomerRepository customerRepository;


    JWTUtils(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public String jwtGeneration(String email){
        Customer customer=customerRepository.findByCustomerEmailId(email);
        if(customer==null){
            throw new RuntimeException("User not found");
        }
        return Jwts.builder().setSubject(email).signWith(secretKey).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis()+expertiationTime)).compact();
    }

    public String getUserEmail(String token){
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean verifyToken(String token,String email){
        try{
            String userEmail = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
            return userEmail.equals(email);
        }
        catch(Exception e){
            return false;
        }
    }
    
}
