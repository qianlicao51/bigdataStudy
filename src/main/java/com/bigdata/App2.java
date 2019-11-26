package com.bigdata;

import java.net.URI;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App2 {
	static Logger logger = LoggerFactory.getLogger(App2.class);

	public static void main(String[] args) throws Exception {
		while (true) {

			Thread.sleep(1000L);
			// TODO 格式化中间插入字母
			log.info("主程序执行 |{}", new DateTime().toString("yyyy-MM-dd'T'HH:mm:ss:SSS"));
			// TODO ClassLoader.getSystemResource 开始的位置是不带"/"
			URI uri = ClassLoader.getSystemResource("hbase/hbase-site.xml").toURI();
			logger.error(uri.toString());

		}
	}
}
