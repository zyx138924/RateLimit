package com.zyx.limiter;

import java.util.concurrent.atomic.AtomicInteger;

public class KerberosRateLimiter implements RateLimiter {

    private static AtomicInteger cache;

    public KerberosRateLimiter() {
        cache = new AtomicInteger(5);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (cache.get() == 5)
                            Thread.sleep(1000);
                        else {
                            cache.addAndGet(1);
                            Thread.sleep(1000);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 取令牌
     *
     * @param permits
     * @return
     */
    @Override
    public synchronized int acquire(int permits) {
        if (cache.get() > permits) {
            cache.decrementAndGet();
            return permits;
        }
        return -1;
    }
}
