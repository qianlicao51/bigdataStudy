package com.bigdata.hbase.booka.filter;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
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

	/**
	 * 单列值过滤器
	 * <p>
	 * <b>缺点</b>
	 * 单列值过滤器在发现该行记录没有我们想要比较的列时，会把整行记录放入结果集。如果想要安全地使用单列值过滤器，请务必保证每行记录都包含要比较的列
	 * <p>
	 * 
	 * @throws Exception
	 */
	@Test
	public void singleColumnValueFilter() throws Exception {
		SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(Bytes.toBytes("cf"), Bytes.toBytes("name"), CompareFilter.CompareOp.EQUAL, new SubstringComparator("tomAndJack"));
		Scan scan = new Scan();
		scan.setFilter(singleColumnValueFilter);
		ResultScanner re = table.getScanner(scan);
		for (Result rs : re) {
			String scanFilterName = Bytes.toString(rs.getValue(Bytes.toBytes("cf"), Bytes.toBytes("name")));
			log.info("scan filter name is :{}", scanFilterName);
		}

	}

	@Test
	public void filterList() throws Exception {
		// 创建过滤器列表
		FilterList filterList = new FilterList(Operator.MUST_PASS_ALL);

		// 只有列族为 cf的记录才放入结果集
		FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("cf")));

		filterList.addFilter(familyFilter);

		// 只有列为name的记录才放入结果集
		QualifierFilter colfFilter = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("name")));
		filterList.addFilter(colfFilter);

		// 只要值为tomAndJack才放入过滤器
		ValueFilter valueFilter = new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("tomAndJack"));
		filterList.addFilter(valueFilter);

		Scan scan = new Scan();
		scan.setFilter(filterList);
		ResultScanner re = table.getScanner(scan);
		for (Result rs : re) {
			String scanFilterName = Bytes.toString(rs.getValue(Bytes.toBytes("cf"), Bytes.toBytes("name")));
			log.info("scan filter name is :{}", scanFilterName);
		}
	}

	/**
	 * 相当于SQL中“=”
	 * 
	 * @throws Exception
	 */
@Test
public void binaryComparator() throws Exception {
	Filter filter = new SingleColumnValueFilter(Bytes.toBytes("cf"), Bytes.toBytes("name"), CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("tomAndJack88")));
	Scan scan = new Scan();
	scan.setFilter(filter);
	ResultScanner re = table.getScanner(scan);
	for (Result rs : re) {
		String scanFilterName = Bytes.toString(rs.getValue(Bytes.toBytes("cf"), Bytes.toBytes("name")));
		log.info("scan filter name is :{}", scanFilterName);
	}
}
}
