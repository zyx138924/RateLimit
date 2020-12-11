package com.zyx.annotation;



import com.zyx.limiter.KerberosRateLimiter;
import com.zyx.limiter.RateLimiter;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({RateLimiterSelector.class})
public @interface EnableRateLimit {
    Class<? extends RateLimiter> value() default KerberosRateLimiter.class;
}
