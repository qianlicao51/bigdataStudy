package com.bigdata.zookeeper;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName HelloZK
 * @Description zookeeper
 * @Author 51
 * @Date 2019年11月29日 上午8:29:35
 */
@Slf4j
public class HelloZK {
	static {
		System.setProperty("names", "51");
		System.setProperty("hobby", "zhuzi51");
		log.info(StringUtils.center("set value finished", 51, "*"));
	}

	public static void main(String[] args) throws InterruptedException {
		createSessionSample();
	}

	/**
	 * 创建一个客户端
	 * 
	 * @throws InterruptedException
	 */
	public static void createSessionSampleFluent() throws InterruptedException {

		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.builder()//
				.connectString("quickstart.cloudera:2181")//
				.sessionTimeoutMs(5000)//
				.retryPolicy(retryPolicy)//
				.build();

		client.start();// 创建会话
		Thread.sleep(Integer.MAX_VALUE);
		int version = 1;
		Stat stat;
		
	}

	/**
	 * 含隔离空间的会话<br>
	 * <p>
	 * 这个非常有用，在使用zookeeper的过程中，经常遇到一个异常就是不存在父节点创建子节点。因此不得不每次创建节点之前判断一下父节点是否存在。
	 * 需要注意的是，由于zookeeper规定了所有非叶子节点必须为持久化节点，调用上面API之后，只有path参数对应的数据节点是临时节点，其父节点均为持久节点。
	 * </p>
	 * 
	 * @throws Exception
	 */
	public static void createSessionSampleNameSpace() throws Exception {

		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.builder()//
				.connectString("quickstart.cloudera:2181")//
				.sessionTimeoutMs(5000)//
				.retryPolicy(retryPolicy)//
				.namespace("base").build();

		client.start();// 创建会话
		Thread.sleep(Integer.MAX_VALUE);
		String path = client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/tmp/test");

	}

	public static void createSessionSample() throws InterruptedException {

		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
//		创建一个客户端实例后，并没有 完成会话的创建，需要调用start 来完成会话创建
		CuratorFramework client = CuratorFrameworkFactory.newClient("quickstart.cloudera:2181", 5000, 3000, retryPolicy);
		client.start();// 创建会话
		Thread.sleep(Integer.MAX_VALUE);
	}

	public static void inteTest() {
		Integer integer = Integer.getInteger("grq", 51);
		log.info("{}", System.getenv("JAVA_HOME"));
		log.info("{}", System.getProperty("user.home"));
		log.info("{}", Integer.getInteger("names"));
		log.info("{}", Integer.getInteger("hobby"));
		Integer integer2 = Integer.getInteger("grq");

		log.info("grq:{}", integer2);
		log.info("{}");

	}
}
