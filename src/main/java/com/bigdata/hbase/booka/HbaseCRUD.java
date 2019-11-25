package com.bigdata.hbase.booka;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName HbaseCRUD
 * @Description HBASE CRUD demo
 * @Author 51
 * @Date 2019年11月25日 下午12:28:32 <br>
 * 
 */
@Slf4j
public class HbaseCRUD {
    private static final Logger logger = LoggerFactory.getLogger(HbaseCRUD.class);
	/**
	 * 创建 Hadoop以及hbase管理对象的配置文件
	 */
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
		// zookeeper地址
		conf.set("hbase.zookeeper.property.clientPort", "2181");// zookeeper端口

		// TODO 实际操作中下面这个没设置，hbase是如何找到hadoop的？
		// conf.set("hbase.rootdir", "hdfs://ncst:9000/hbase");
		try {
			connection = ConnectionFactory.createConnection(conf);
			table = connection.getTable(TableName.valueOf(tableNameStr));
			logger.info(StringUtils.center("get table ", 20, '*'));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("初始化出现错误");
		}
	}

	public static void putDate() throws IOException {
		Put put = new Put(Bytes.toBytes("row2"));
		put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("name"), Bytes.toBytes("jack"));
		table.put(put);
	}

	public static void main(String[] args) throws IOException {
		putDate();
	}
}
