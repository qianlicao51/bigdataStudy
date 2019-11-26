package com.bigdata.hbase.booka.filter;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bigdata.hbase.booka.HBaseUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName FilterDemo
 * @Description hbase过滤器
 * @Author 51
 * @Date 2019年11月26日 下午4:44:10
 */
@Slf4j
public class FilterDemo {
	Table table;

	@Before
	public void runBeforeTestMethod() throws IOException {
		table = HBaseUtils.getTale("javatab");
		System.out.println(table.getTableDescriptor());
	}

	@After
	public void runAfterTestMethod() {
		HBaseUtils.closed(table);
		log.info(StringUtils.center("after", 18, "-"));

	}

	/**
	 * 值过滤器
	 * <p>
	 * 挑选出所有列族cf:name包含`tomAndJack`字符串的记录，在sql中等于`cf:name like '%tomAndJack%'`
	 * 。现在要在原来使用Scan查询记录的基础上增加filter的设置。首先是构造ValueFilter 函数。
	 * <p>
	 * CompareFilter中包含的一个枚举类：CompareOp
	 * <p>
	 * SubstringComparator是一个比较器，这个比较器可以判断目标字符串是否包含所指定的字符串
	 * <p>
	 * 
	 * @throws IOException
	 */
@Test
public void valueFilter() throws IOException {
	ValueFilter valueFilter = new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("tomAndJack"));
	Scan scan = new Scan();
	scan.setFilter(valueFilter);
	ResultScanner re = table.getScanner(scan);
	for (Result rs : re) {
		String scanFilterName = Bytes.toString(rs.getValue(Bytes.toBytes("cf"), Bytes.toBytes("name")));
		log.info("scan filter name is :{}", scanFilterName);
	}

}
}
