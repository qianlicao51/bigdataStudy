package com.bigdata.hbase.booka;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName HBaseBatchDemo
 * @Description HBase批量操作
 * @Author 51
 * @Date 2019年11月26日 下午2:46:34
 */
@Slf4j
public class HBaseBatchDemo {
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
		conf.set("hbase.zookeeper.property.clientPort", "2181");// zookeeper端口
		try {
			connection = ConnectionFactory.createConnection(conf);
			table = connection.getTable(TableName.valueOf(tableNameStr));
			logger.info(StringUtils.center("get table ", 20, '*'));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("初始化出现错误");
		}
	}

	/**
	 * hbase批量操作
	 * <p>
	 * put get delete 都实现了Row接口，也就是说这里的操作列表actions里面的操作可以是Put Get
	 * Delete中的任意一种；第二个参数是results的操作的结果，results中的结果顺序是跟传入的操作列表顺序是一一对应的。
	 * </p>
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
public static void addBatch() throws IOException, InterruptedException {
	List<Row> actions = new ArrayList<Row>();

	Get get = new Get(Bytes.toBytes("row2"));
	actions.add(get);

	Put put = new Put(Bytes.toBytes("row8"));
	put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("name"), Bytes.toBytes("tomAndJack88"));

	actions.add(put);

	Delete delete = new Delete(Bytes.toBytes("row5"));

	actions.add(delete);

	Object[] results = new Object[actions.size()];
	table.batch(actions, results);
	for (Object object : results) {
		Result result = (Result) object;
		log.info("result is {}", Bytes.toString(result.getValue(Bytes.toBytes("cf"), Bytes.toBytes("name"))));
	}
}

	public static void main(String[] args) throws Exception {
		addBatch();
		try {
			table.close();
			connection.close();
		} catch (IOException e) {
			log.error("关闭流失败");
		}
	}
}
