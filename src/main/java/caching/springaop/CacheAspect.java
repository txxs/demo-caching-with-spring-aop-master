package caching.springaop;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;


/**
 * 处理MyCacheable方法的切面
 * @author txxs
 */
@Aspect
public class CacheAspect {

	private Logger logger = Logger.getLogger(CacheAspect.class);
	private Map<String, Object> cache;

	public CacheAspect() {
		cache = new HashMap<String, Object>();
	}

	/**
	 * 所有标注了@Cacheable标签的方法切入点
	 */
	@Pointcut("execution(@Cacheable * *.*(..))")
	@SuppressWarnings("unused")
	private void cache() {
	}

	@Around("cache()")
	public Object aroundCachedMethods(ProceedingJoinPoint thisJoinPoint)
			throws Throwable {
		logger.debug("Execution of Cacheable method catched");
		
		MethodSignature signature = (MethodSignature) thisJoinPoint.getSignature();
		String methodName = signature.getMethod().getName();
		Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
		Annotation[][] annotations = thisJoinPoint.getTarget().getClass().
				getMethod(methodName,parameterTypes).getParameterAnnotations();
		//获得标签，再根据标签获得名字，再根据是哪个名字获取相应的缓存，然后再根据获取的参数删除对应缓存中的数据
		Cacheable cacheable = signature.getMethod().getAnnotation(Cacheable.class);
		String cacheName = cacheable.cacheName();
		
		//产生缓存数据的key值，像是这个样子caching.aspectj.Calculator.sum(Integer=1;Integer=2;)
		StringBuilder keyBuff = new StringBuilder();
		//增加类的名字
		keyBuff.append(thisJoinPoint.getTarget().getClass().getName());
		//加上方法的名字
		keyBuff.append(".").append(thisJoinPoint.getSignature().getName());
		keyBuff.append("(");
		//循环出cacheable方法的参数
		for (final Object arg : thisJoinPoint.getArgs()) {
			//增加参数的类型和值
			keyBuff.append(arg.getClass().getSimpleName() + "=" + arg + ";");
		}
		keyBuff.append(")");
		String key = keyBuff.toString();
		logger.debug("Key = " + key);
		Object result = cache.get(key);
		if (result == null) {
			logger.debug("Result not yet cached. Must be calculated...");
			result = thisJoinPoint.proceed();
			logger.info("Storing calculated value '" + result + "' to cache");
			cache.put(key, result);
		} else {
			logger.debug("Result '" + result + "' was found in cache");
		}
		return result;
	}

}
