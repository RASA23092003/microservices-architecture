package com.infy.authentication;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JWTFilter extends OncePerRequestFilter{
    private final JWTUtils jwtUtility;

    JWTFilter(JWTUtils jwtUtility) {
        this.jwtUtility = jwtUtility;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header=request.getHeader("Authorization");
        String email=null;
        String token=null;
        if(header!=null && header.startsWith("Bearer ")){
            token=header.substring(7);
            email=jwtUtility.getUserEmail(token);
        }
        if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            if(jwtUtility.verifyToken(token, email)){
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
    
}
