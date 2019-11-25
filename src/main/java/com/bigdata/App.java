package com.bigdata;

import java.net.URI;
import java.net.URISyntaxException;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
	static Logger logger = LoggerFactory.getLogger(App2.class);

	public static void main(String[] args) throws URISyntaxException {

		log.info("主程序执行 |{}", new DateTime().toString("yyyy-MM-dd HH:mm:ss:SSS"));
		logger.info("主程序执行logger |{}", new DateTime().toString("yyyy-MM-dd HH:mm:ss:SSS"));
		URI uri = ClassLoader.getSystemResource("application.properties").toURI();
		System.out.println(uri.toString());
	}

}
