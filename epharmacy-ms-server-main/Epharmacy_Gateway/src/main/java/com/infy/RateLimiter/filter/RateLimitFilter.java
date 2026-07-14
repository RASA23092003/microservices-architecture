package com.infy.RateLimiter.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.infy.RateLimiter.service.LeakeyBucketService;
import com.infy.RateLimiter.service.TokenBucketService;

import reactor.core.publisher.Mono;

@Component
public class RateLimitFilter implements GlobalFilter {

    //private final LeakeyBucketService service;
    private final TokenBucketService tokenBucketService;

    // public RateLimitFilter(
    //         LeakeyBucketService service) {

    //     this.service = service;
    //      System.out.println("RateLimitFilter Bean Created");
    // }
    public RateLimitFilter(
           TokenBucketService tokenBucketService) {
        this.tokenBucketService = tokenBucketService;
         System.out.println("RateLimitFilter Bean Created");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("RateLimitFilter Executed");
        String ip =exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();

        if (!tokenBucketService.tokenAllow(ip, 1)) {

            exchange.getResponse().setStatusCode( HttpStatus.TOO_MANY_REQUESTS);

            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}
