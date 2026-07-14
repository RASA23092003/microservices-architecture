package com.infy.authentication;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.infy.entity.Customer;
import com.infy.repository.CustomerRepository;

@Service
public  class CustomUserDetail implements UserDetailsService{
    private final CustomerRepository customerRepository;

    CustomUserDetail(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByCustomerEmailId(email);
        if (customer == null) {
            throw new UsernameNotFoundException("Customer not found");
        }
        return User.builder().username(customer.getCustomerEmailId()).password(customer.getPassword()).authorities(Collections.emptyList()).build();
        
    }


}
    

