package caching.springaop;

import org.apache.log4j.Logger;

/**
 * 时间计算类
 * @author Igor Urmincek
 * @author txxs
 */
public class Calculator {
	private Logger logger = Logger.getLogger(Calculator.class);
	
	@Cacheable(key="",cacheName="ds")
	public int sum(int a, int b) {
		logger.info("Calculating " + a + " + " + b);
		try {
			//假设这是代价非常高的计算
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			logger.error("Something went wrong...", e);
		}
		return a + b;
	}
}
