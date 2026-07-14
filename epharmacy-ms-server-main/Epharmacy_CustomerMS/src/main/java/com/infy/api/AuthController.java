package com.infy.api;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infy.authentication.JWTUtils;
import com.infy.dto.CustomerDTO;

import com.infy.service.CustomerService;

@Controller
@RequestMapping(value = "/customer-api")
public class AuthController {
    private final JWTUtils jwtUtil;
    private CustomerService customerService;
    private AuthenticationManager authenticationManager;

    AuthController(JWTUtils jwtUtil, CustomerService customerService, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/customer/auth/register")
    public ResponseEntity<String> registerCustomer(@RequestBody @Valid CustomerDTO customerDTO) throws Exception {
        String successMessage = customerService.authregisterNewCustomer(customerDTO);
        return new ResponseEntity<>(successMessage, HttpStatus.CREATED);
    }

    @PostMapping(value = "/customer/auth/login")
    public ResponseEntity<String> authenticateCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            System.out.println("========== LOGIN CONTROLLER ==========");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            customerDTO.getCustomerEmailId(), customerDTO.getPassword()));
            System.out.println("========== AUTH SUCCESS ==========");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        String jwtKey = jwtUtil.jwtGeneration(customerDTO.getCustomerEmailId());
        System.out.println("========== JWT GENERATED ==========");

        return new ResponseEntity<>(jwtKey, HttpStatus.OK);
    }

}
