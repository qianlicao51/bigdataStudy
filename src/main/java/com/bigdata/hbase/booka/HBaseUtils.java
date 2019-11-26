package com.bigdata.hbase.booka;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName HBaseUtils
 * @Description
 * @Author 51
 * @Date 2019年11月26日 下午3:43:45
 */
@Slf4j
public class HBaseUtils {
	private HBaseUtils() {

	}

	public static Configuration conf;
	public static Admin admin;
	static Connection connection;
	static Table table;
	static String tableNameStr = "javatab";
	static {
		// 使用HBaseConfiguration 单例方法实例化
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "quickstart.cloudera");// 单机
		// conf.set("hbase.zookeeper.quorum", "master,work1,work2");//
		conf.set("hbase.zookeeper.property.clientPort", "2181");// zookeeper端口

		// TODO 实际操作中下面这个没设置，hbase是如何找到hadoop的？
		// conf.set("hbase.rootdir", "hdfs://ncst:9000/hbase");
		try {
			connection = ConnectionFactory.createConnection(conf);
			admin = connection.getAdmin();
			log.info(StringUtils.center("get table ", 20, '*'));
		} catch (IOException e) {
			e.printStackTrace();
			log.error("初始化出现错误");
		}
	}

	/**
	 * 获取表|没有给定的表就获取默认表javatab
	 * 
	 * @param tabName
	 * @return
	 * @throws IOException
	 */
	public static Table getTale(String tabName) throws IOException {
		try {
			if (admin.tableExists(TableName.valueOf(tabName))) {
				return connection.getTable(TableName.valueOf(tabName));
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.info("获取表 失败{}", e.getMessage());
		}
		return connection.getTable(TableName.valueOf(tableNameStr));
	}

	public static void closed(Table table) {
		try {
			table.close();
			connection.close();
		} catch (IOException e) {
			log.error("关闭流失败");
		}
		log.info(StringUtils.center("finish ………………", 20, "-"));

	}
}
