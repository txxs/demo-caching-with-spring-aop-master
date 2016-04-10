package caching.springaop;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ʹ��SpringAOP����ļ�����
 * @author txxs
 */
public class App {

	private static Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) {
		logger.debug("Starting...");
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-config.xml");
		Calculator calc = (Calculator) ctx.getBean("calc");
		//��������Ľ�����ᱻ�洢��cache
		logger.info("1 + 2 = " + calc.sum(1, 2));
		//�ӻ����л�ȡ���
		logger.info("1 + 2 = " + calc.sum(1, 2));
		logger.debug("Finished!");
	}

}
