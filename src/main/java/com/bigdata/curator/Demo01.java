package com.bigdata.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @ClassName Demo01
 * @Description
 * @Author 51
 * @Date 2019年12月20日 下午1:55:19
 */
public class Demo01 {
	public static void main(String[] args) throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
//	创建一个客户端实例后，并没有 完成会话的创建，需要调用start 来完成会话创建
		CuratorFramework client = CuratorFrameworkFactory.newClient("quickstart.cloudera:2181", 5000, 3000, retryPolicy);
		client.start();// 创建会话

//		client.createContainers("/grq/zk/curator");

//		client.create().forPath("/grq/zk/curator/tmp", "zk测试".getBytes());

	}
}
