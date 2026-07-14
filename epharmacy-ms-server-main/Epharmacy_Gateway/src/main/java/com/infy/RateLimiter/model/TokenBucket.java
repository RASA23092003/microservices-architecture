package com.infy.RateLimiter.model;

import java.time.LocalDateTime;

public class TokenBucket {
    int tokenAvailable;
    LocalDateTime lastUpdated;
    public TokenBucket(int tokenAvailable, LocalDateTime lastUpdated) {
        this.tokenAvailable = tokenAvailable;
        this.lastUpdated = lastUpdated;
    }
    public void setTokenAvailable(int tokenAvailable) {
        this.tokenAvailable = tokenAvailable;
    }
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public int getTokenAvailable() {
        return tokenAvailable;
    }
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
}
