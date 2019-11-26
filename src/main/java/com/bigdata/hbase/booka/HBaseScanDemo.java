package com.bigdata.hbase.booka;

import java.io.IOException;

import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @ClassName HBaseScanDemo
 * @Description Scan扫描
 * @Author 51
 * @Date 2019年11月26日 下午3:50:10
 */
public class HBaseScanDemo {
	static String tabName = "javatab";

	/**
	 * 为什么是getScanner()
	 * <p>
	 * 这是因为scan的结果获取本质跟get不一样，Table通过传入scan之后返回的结果扫描器并不是实际的查询结果。获取结果扫描器的时候并没有真正去查询数据。
	 * 真正获取数据的时候要打开扫描器，然后遍历它。这个时候才真正去查询了数据
	 * </p>
	 * 
	 * @throws IOException
	 */
	public static void scanData() throws IOException {
		Table table = HBaseUtils.getTale("javatab");

		Scan scan = new Scan(Bytes.toBytes("row1"));

		ResultScanner re = table.getScanner(scan);

	}

	public static void main(String[] args) {

	}
}
