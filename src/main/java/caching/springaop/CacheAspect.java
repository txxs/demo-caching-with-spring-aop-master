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
 * ����MyCacheable����������
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
	 * ���б�ע��@Cacheable��ǩ�ķ��������
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
		//��ñ�ǩ���ٸ��ݱ�ǩ������֣��ٸ������ĸ����ֻ�ȡ��Ӧ�Ļ��棬Ȼ���ٸ��ݻ�ȡ�Ĳ���ɾ����Ӧ�����е�����
		Cacheable cacheable = signature.getMethod().getAnnotation(Cacheable.class);
		String cacheName = cacheable.cacheName();
		
		//�����������ݵ�keyֵ�������������caching.aspectj.Calculator.sum(Integer=1;Integer=2;)
		StringBuilder keyBuff = new StringBuilder();
		//�����������
		keyBuff.append(thisJoinPoint.getTarget().getClass().getName());
		//���Ϸ���������
		keyBuff.append(".").append(thisJoinPoint.getSignature().getName());
		keyBuff.append("(");
		//ѭ����cacheable�����Ĳ���
		for (final Object arg : thisJoinPoint.getArgs()) {
			//���Ӳ��������ͺ�ֵ
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
