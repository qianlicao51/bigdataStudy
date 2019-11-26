package com.bigdata.hbase;

import java.io.IOException;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloHbase {

	/**
	 * 创建 Hadoop以及hbase管理对象的配置文件
	 */
	public static Configuration conf;
	public static Admin admin;
	static Connection connection;
	static {
		// 使用HBaseConfiguration 单例方法实例化
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "quickstart.cloudera");// 单机
		// conf.set("hbase.zookeeper.quorum", "master,work1,work2");//
		// zookeeper地址
		conf.set("hbase.zookeeper.property.clientPort", "2181");// zookeeper端口

		// TODO 实际操作中下面这个没设置，hbase是如何找到hadoop的？
		// conf.set("hbase.rootdir", "hdfs://ncst:9000/hbase");
		try {
			// admin = new HBaseAdmin(conf);
			// Step 1 |创建连接
			connection = ConnectionFactory.createConnection(conf);
			admin = connection.getAdmin();
			log.info("connection hbase success");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("初始化出现错误");
		}
	}

	/**
	 * 建表|hbase shell中可以通过 list describe 查看
	 */
	public static void createTable() {
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "quickstart.cloudera");// 单机

		conf.set("hbase.zookeeper.property.clientPort", "2181");// zookeeper端口

		// TODO 实际操作中下面这个没设置，hbase是如何找到hadoop的？
		// conf.set("hbase.rootdir", "hdfs://ncst:9000/hbase");
		try {
			// Step 1 |创建连接
			connection = ConnectionFactory.createConnection(conf);
			log.info("connection hbase success");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("初始化出现错误");
		}

		// Step 2 |定义表名
		TableName tableName = TableName.valueOf("javatb");

		// Step 3 |定义表
		HTableDescriptor table = new HTableDescriptor(tableName);

		// Step 4 |定义列族
		HColumnDescriptor columnDescriptor = new HColumnDescriptor("mycf");
		table.addFamily(columnDescriptor);

		try {
			admin = connection.getAdmin();
			// Step 5 |执行创建表动作
			admin.createTable(table);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				admin.close();
				connection.close();
			} catch (IOException e) {
				log.error("关闭流失败");
			}

		}
	}

	/**
	 * 表是否存在
	 * 
	 * @param tableName
	 * @throws IOException
	 */
	public static void tableExists(String tableName) {
		TableName table = TableName.valueOf(tableName);

		try {
			if (admin.tableExists(table)) {
				admin.disableTable(table);
				admin.deleteTable(table);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("执行完毕");
	}

	/**
	 * 创建表
	 * 
	 * @param tableNam 表名字
	 * @param column   列族名字 <br>
	 */
	public static void createOrOverwrite(String tableNam, String column) {
		TableName tableName = TableName.valueOf(tableNam);
		try {
			if (admin.tableExists(tableName)) {
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// Step 3 |定义表
		HTableDescriptor table = new HTableDescriptor(tableName);

		// Step 4 |定义列族
		HColumnDescriptor columnDescriptor = new HColumnDescriptor(column);
		table.addFamily(columnDescriptor);
		try {
			// Step 5 |执行创建表动作
			admin.createTable(table);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				admin.close();
				connection.close();
			} catch (IOException e) {
				log.error("关闭流失败");
			}
			log.info("finished ………………");
		}
	}

	public static void compressCloum(String tableNameStr, String column) {
		TableName tableName = TableName.valueOf(tableNameStr);
		try {

			// Step 3 |获取表
			HTableDescriptor table = admin.getTableDescriptor(tableName);
			// Step 4 |定义列族
			HColumnDescriptor mycf = new HColumnDescriptor(column);

			Collection<HColumnDescriptor> families = table.getFamilies();
			for (HColumnDescriptor hColumnDescriptor : families) {
				System.out.println(hColumnDescriptor.getNameAsString());

			}
			mycf.setCompressionType(Algorithm.SNAPPY);
			// 把列族 的定义更新到表定义里面去||只有调用了Admin类来进行操作的时候hbase的修改才开始真正执行

			table.modifyFamily(mycf);

			admin.modifyTable(tableName, table);

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		log.info(StringUtils.center("main", 20, "-"));
		tableExists("javatb");
		// compressCloum("javatab", "cf");
		// tableDelete("javatab");

		// createOrOverwrite("javatab", "cf");
		try {
			admin.close();
			connection.close();
		} catch (IOException e) {
			log.error("关闭流失败");
		}
		log.info(StringUtils.center("finish ………………", 20, "-"));
	}

	/**
	 * 删除表
	 * 
	 * @param tableNameStr 表名字
	 */
	private static void tableDelete(String tableNameStr) {
		TableName tableName = TableName.valueOf(tableNameStr);
		try {
			if (admin.tableExists(tableName)) {
				// 如果表存在就删除掉
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
