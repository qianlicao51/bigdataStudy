package com.bigdata.hbase.booka;

import java.net.URISyntaxException;
import java.security.interfaces.RSAKey;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName BookDemo
 * @Description HBASE不睡觉书中demo
 * @Author 51
 * @Date 2019年11月26日 下午5:20:39
 */
@Slf4j
public class BookDemo {
	public static void main(String[] args) throws URISyntaxException {
		Configuration config = HBaseConfiguration.create();
		// 添加配置文件(hbase-site.xml和core-site.xml)
		config.addResource(new Path(ClassLoader.getSystemResource("hbase/hbase-site.xml").toURI()));
		config.addResource(new Path(ClassLoader.getSystemResource("hbase/hbase-site.xml").toURI()));
		// config.set("hbase.zookeeper.quorum", "quickstart.cloudera");// 单机
		try (Connection connection = ConnectionFactory.createConnection(config)) {
			Table table = connection.getTable(TableName.valueOf("javatab"));
			ValueFilter valueFilter = new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("tomAndJack"));
			Scan scan = new Scan();
			scan.setFilter(valueFilter);
			ResultScanner re = table.getScanner(scan);
			for (Result rs : re) {
				String scanFilterName = Bytes.toString(rs.getValue(Bytes.toBytes("cf"), Bytes.toBytes("name")));
				log.info("scan filter name is :{}", scanFilterName);
			}
			re.close();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
