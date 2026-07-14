package com.infy.RateLimiter.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.infy.RateLimiter.model.TokenBucket;
@Service
public class TokenBucketService{
    private static int capacity=10;
    private static int fillRate=2;
    private final static Map<String,TokenBucket> bucketMap=new ConcurrentHashMap<>();


    public static boolean tokenAllow(String ip,int requestCount){
        TokenBucket bucket = bucketMap.computeIfAbsent(
    ip,
    k -> new TokenBucket(capacity, LocalDateTime.now())
);
        synchronized (bucket) {
            long elapsed=Duration.between(bucket.getLastUpdated(),LocalDateTime.now()).getSeconds();
            int tokens=Math.min(capacity,bucket.getTokenAvailable()+(int)(elapsed*fillRate));
            if(tokens<requestCount){
                bucket.setTokenAvailable(tokens);
                bucket.setLastUpdated(LocalDateTime.now());
                return false;
            }
            tokens-=requestCount;
            bucket.setTokenAvailable(tokens);
            bucket.setLastUpdated(LocalDateTime.now());
            return true;
    }
    }
}