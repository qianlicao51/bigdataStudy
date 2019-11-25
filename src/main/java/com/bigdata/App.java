package com.bigdata;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
	static Logger logger = LoggerFactory.getLogger(App2.class);

	public static void main(String[] args) throws URISyntaxException {
		// TODO 打印 Logback 内部状态
//		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//		StatusPrinter.print(lc);

		log.info("主程序执行 |{}", new DateTime().toString("yyyy-MM-dd HH:mm:ss:SSS"));
		logger.info("主程序执行logger |{}", new DateTime().toString("yyyy-MM-dd HH:mm:ss:SSS"));

		System.err.println("******************************************");
		Thread currentThread = Thread.currentThread();
		URL resource3 = currentThread.getContextClassLoader().getResource("application.properties");
		URL resource = currentThread.getClass().getResource("/application.properties");
		System.out.println(resource.getPath().toString());
		URI uri = ClassLoader.getSystemResource("application.properties").toURI();
	}

	/**
	 * TODO https://blog.csdn.net/z69183787/article/details/22774537<br>
	 * <p>
	 * getContextClassLoader().getResource("") ==
	 * getClass().getResource("/")|注意斜杆和引号
	 * </p>
	 * 
	 */
	public static void name() {

		System.err.println("******************************************");
		Thread currentThread = Thread.currentThread();
		URL resource3 = currentThread.getContextClassLoader().getResource("");
		URL resource = currentThread.getClass().getResource("/");
		log.info(resource3.getPath().toString());

		log.info(resource.getPath().toString());
	}
}
