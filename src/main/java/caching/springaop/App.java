package caching.springaop;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 使用SpringAOP缓存的简单例子
 * @author txxs
 */
public class App {

	private static Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) {
		logger.debug("Starting...");
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
		Calculator calc = (Calculator) ctx.getBean("calc");
		//计算出来的结果将会被存储在cache
		logger.info("1 + 2 = " + calc.sum(1, 2));
		//从缓存中获取结果
		logger.info("1 + 2 = " + calc.sum(1, 2));
		logger.debug("Finished!");
	}

}
