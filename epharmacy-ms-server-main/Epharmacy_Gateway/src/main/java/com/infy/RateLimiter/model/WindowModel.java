package com.infy.RateLimiter.model;

import java.time.LocalDateTime;

public class WindowModel {
    private int requestCount;
    private LocalDateTime windowStartTime;
    public WindowModel(int requestCount, LocalDateTime windowStartTime) {
        this.requestCount = requestCount;
        this.windowStartTime = windowStartTime;
    }
    public int getRequestCount() {
        return requestCount;
    }
    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }
    public LocalDateTime getWindowStartTime() {
        return windowStartTime;
    }
    public void setWindowStartTime(LocalDateTime windowStartTime) {
        this.windowStartTime = windowStartTime;
    }
    
}
