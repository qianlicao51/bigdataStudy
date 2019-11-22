package com.bigdata;

import org.joda.time.DateTime;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App2 {
	public static void main(String[] args) {
		log.info("主程序执行 |{}", new DateTime().toString("yyyy-MM-dd HH:mm:ss:SSS"));

	}
}
