package com.bigdata.hbase.booka;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
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

	/**
	 * 添加数据
	 * <p>
	 * hbase shell get 'javatab','row2',{COLUMN=>'cf:name',VERSIONS=>5}
	 * </p>
	 * 
	 * @throws IOException
	 */
	public static void putDate() throws IOException {
		Put put = new Put(Bytes.toBytes("row2"));
		put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("name"), Bytes.toBytes("tomAndJack"));
		table.put(put);
	}

	/**
	 * put语法糖
	 */
	public static void putSugar() throws IOException {
		Put put = new Put(Bytes.toBytes("row5"));
		put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("name"), Bytes.toBytes("测试1"))//
				.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("age"), Bytes.toBytes(26L))//
				.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("hobby"), Bytes.toBytes("read"));
		table.put(put);
	}

	/**
	 * <p>
	 * 问题：在读出数据之后和修改数据中间这段时间，如果有人修改了这个数据，就会发生数据不一致的问题。使用传统关系型数据库的时候，
	 * 针对这种场景是有对策的，就是在每次修改之前先快速查询一次，对照一下查询出来的数据是否更之前我们阅读到的数据一致，如果一致
	 * 就修改数据。不一致就提示用户再次加载页面以阅读新的数据
	 * </p>
	 * <p>
	 * 解决：checkAndPut
	 * </p>
	 * 
	 * @throws IOException
	 */
	public static void checkPutData() throws IOException {
		Put put = new Put(Bytes.toBytes("row2"));
		put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("name"), Bytes.toBytes("tomAndJack"));

		boolean checkAndPut = table.checkAndPut(Bytes.toBytes("row2"), Bytes.toBytes("cf"), Bytes.toBytes("name"), Bytes.toBytes("go_java"), put);
		log.info("checkAndPut result is {}", checkAndPut);
	}

	/**
	 * increment|执行加减操作
	 * 
	 * @throws IOException
	 */
	public static void incrementDemo() throws IOException {
		Increment inc = new Increment(Bytes.toBytes("row5"));
		inc.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("age"), 26L);
		table.increment(inc);
	}

	/************** Get *******************/
	public static void getData() throws IOException {
		Get get = new Get(Bytes.toBytes("row5"));
		Result result = table.get(get);
		/**
		 * <p>
		 * 从Result对象中用getValue方法获取到数据，getValue需 要的参数是列族（column family）和列（column）：
		 * </p>
		 * <p>
		 * 用HBase API提供的Bytes工具类把byte[]转化为 其他类型：|严格按照存储的值转换(存储为Long 取出来转为Int 得到的是0)
		 * </p>
		 */
		byte[] age = result.getValue(Bytes.toBytes("cf"), Bytes.toBytes("age"));
		log.info("");
		log.info("hbase java api get data is :{}", Bytes.toLong(age));
		log.info("");

	}

	/**
	 * 可以通过设置MAX_VERSIONS来获取同一列里的多个版本的数据，但是Result.getValue方法只能获得最新的一个数据，如果要获取多个数据就要使用到Cell接口。
	 * <p>
	 * 1 先设置Get的MAX_VERSIONS为10<br>
	 * </p>
	 * 
	 * @throws IOException
	 */
	public static void cellMentod() throws IOException {
		// Step1|1 先设置Get的MAX_VERSIONS为10
		Get get = new Get(Bytes.toBytes("row5"));
		get.setMaxVersions(10);

		// Step2| 查询得到Reuslt对象
		Result result = table.get(get);

		// Step3| 获取这个列的多个版本值(早期的getColumn()方法已经被废弃)
		List<Cell> columnCells = result.getColumnCells(Bytes.toBytes("cf"), Bytes.toBytes("age"));

		for (Cell cell : columnCells) {
			// 用 CellUtil.cloneValue来获取数据而不是getValue
			byte[] cloneValue = CellUtil.cloneValue(cell);
			log.info("获取的数据" + Bytes.toLong(cloneValue));
		}
	}

	public static void main(String[] args) throws IOException {
		// putSugar();
		// incrementDemo();
		// getData();
		cellMentod();
		try {
			table.close();
			connection.close();
		} catch (IOException e) {
			log.error("关闭流失败");
		}
		log.info(StringUtils.center("finish ………………", 20, "-"));
	}
}
