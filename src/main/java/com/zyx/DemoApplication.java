package com.zyx;

import com.zyx.annotation.EnableRateLimit;
import com.zyx.limiter.SemaphoreRateLimiter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableRateLimit(SemaphoreRateLimiter.class)
public class DemoApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(DemoApplication.class, args);
    }

}
