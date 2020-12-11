package com.zyx.limiter;

import java.util.concurrent.Semaphore;

public class SemaphoreRateLimiter implements RateLimiter {

    /**
     * 存储限流量和方法,必须是static且线程安全,保证所有线程进入都唯一
     */
    private Semaphore permit;

    private Integer nums = 5;

    public SemaphoreRateLimiter() {
        permit = new Semaphore(nums);
    }
    public SemaphoreRateLimiter(int permitNums) {
        this.nums = permitNums;
        permit = new Semaphore(permitNums);
    }
    @Override
    public int acquire(int permits) {
        return permit.tryAcquire() ? 1 : 0;
    }

    public int acquire() {
        return acquire(1);
    }
}
