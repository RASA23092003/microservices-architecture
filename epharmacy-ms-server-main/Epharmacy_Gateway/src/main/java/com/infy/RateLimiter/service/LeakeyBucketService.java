package com.infy.RateLimiter.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.infy.RateLimiter.model.LeakeyBucket;

//@Service
public class LeakeyBucketService {

    private final Map<String, LeakeyBucket> buckets =
            new ConcurrentHashMap<>();

    private static final int CAPACITY = 3;
    private static final int LEAK_RATE = 1;

    private void leak(LeakeyBucket bucket) {

        long now = System.currentTimeMillis();

        long elapsedSeconds =
                (now - bucket.getLastLeakTime()) / 1000;

        int leakedRequests =
                (int) elapsedSeconds * LEAK_RATE;

        if (leakedRequests > 0) {

            bucket.setCurrentSize(
                    Math.max(
                            0,
                            bucket.getCurrentSize()
                                    - leakedRequests));

            bucket.setLastLeakTime(now);
        }
    }

    public boolean allowRequest(String key) {
        buckets.putIfAbsent(key,new LeakeyBucket(0,System.currentTimeMillis()));

        LeakeyBucket bucket = buckets.get(key);

        synchronized (bucket) {
            leak(bucket);

            System.out.println(
                    "Before Increment : "
                            + bucket.getCurrentSize());

            if (bucket.getCurrentSize() >= CAPACITY) {

                System.out.println(
                        "RATE LIMIT EXCEEDED");

                return false;
            }

            bucket.setCurrentSize(
                    bucket.getCurrentSize() + 1);

            System.out.println(
                    "After Increment : "
                            + bucket.getCurrentSize());

            return true;
        }
    }

}