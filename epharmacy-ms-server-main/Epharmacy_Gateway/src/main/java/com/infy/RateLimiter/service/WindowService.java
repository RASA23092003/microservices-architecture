package com.infy.RateLimiter.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.infy.RateLimiter.model.WindowModel;

public class WindowService {
    private final long windowSizeInSeconds=60;
    private final int maxRequests=100;
    private final Map<String,WindowModel> requestMap=new ConcurrentHashMap<>();

    public boolean allowRequest(String ip){
        WindowModel window=requestMap.computeIfAbsent(ip, i->new WindowModel(0, LocalDateTime.now()));
        synchronized(window){
            long elapsed=Duration.between(window.getWindowStartTime(),LocalDateTime.now()).getSeconds();
            if(elapsed>=windowSizeInSeconds){
                window.setRequestCount(0);
                window.setWindowStartTime(LocalDateTime.now());
            }
            if(window.getRequestCount()>=maxRequests){
                return false;
            }
            window.setRequestCount(window.getRequestCount()+1);
           
            return true;
        }
    }
    
}
