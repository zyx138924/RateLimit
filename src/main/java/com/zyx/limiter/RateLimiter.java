package com.zyx.limiter;


import org.springframework.stereotype.Service;

@Service
public interface RateLimiter {
    int acquire(int permits);
}
