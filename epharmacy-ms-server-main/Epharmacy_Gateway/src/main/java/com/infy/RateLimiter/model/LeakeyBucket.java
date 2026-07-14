package com.infy.RateLimiter.model;

public class LeakeyBucket {
      private int currentSize;

    private long lastLeakTime;

    public LeakeyBucket(int currentSize,
                       long lastLeakTime) {
        this.currentSize = currentSize;
        this.lastLeakTime = lastLeakTime;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public long getLastLeakTime() {
        return lastLeakTime;
    }

    public void setLastLeakTime(long lastLeakTime) {
        this.lastLeakTime = lastLeakTime;
    }
}
