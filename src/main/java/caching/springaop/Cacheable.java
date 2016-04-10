package caching.springaop;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented

public @interface Cacheable {
	String cacheName();
	String key() default "";
	int[] keyArgs() default {};
	String[] keyProperties() default {};
	String keyGenerator() default "";
	int expires() default 1800;
}
