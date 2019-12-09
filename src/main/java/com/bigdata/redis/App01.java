package com.bigdata.redis;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @ClassName App01
 * @Description|https://www.bilibili.com/video/av76235738?p=39|<br>
 *                                                                  |jedis
 * @Author 51
 * @Date 2019年12月9日 上午9:45:18
 */
@Slf4j
public class App01 {
	public static void main(String[] args) {

		log.info("redis App01");
		Jedis jedis = new Jedis("192.168.28.129", 6379);
		String set = jedis.set("javaOps", "java操作");
		String getValue = jedis.get("javaOps");
		log.info("set result:{}", set);
		log.info("getValue is {}", getValue);
		String select = jedis.select(3);
		log.info("select db is:{}", select);
		log.info("page is :{}", jedis.get("page"));
		jedis.set("os", System.getenv("OS"));
		jedis.close();
	}

	@Test
	public void redisSet() throws Exception {
		Jedis jedis = new Jedis("192.168.28.129", 6379);
		String set = jedis.set("javaOps", "java操作");

		String getValue = jedis.get("javaOps");
		log.info("set result:{}", set);
		log.info("getValue is {}", getValue);

		jedis.close();

	}
}
