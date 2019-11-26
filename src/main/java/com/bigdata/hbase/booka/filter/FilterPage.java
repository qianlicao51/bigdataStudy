package com.bigdata.hbase.booka.filter;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.MultiRowRangeFilter;
import org.apache.hadoop.hbase.filter.MultiRowRangeFilter.RowRange;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bigdata.hbase.booka.HBaseUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName FilterPage
 * @Description 分页过滤器
 * @Author 51
 * @Date 2019年11月26日 下午10:15:27
 */
@Slf4j
public class FilterPage {
	Table table;
	Scan scan;

	@Before
	public void runBeforeTestMethod() throws IOException {
		table = HBaseUtils.getTale("javatab");
		System.out.println(table.getTableDescriptor());
		scan = new Scan();
	}

	@After
	public void runAfterTestMethod() throws IOException {
		ResultScanner re = table.getScanner(scan);
		for (Result rs : re) {
			byte[] row = rs.getRow();
			log.info("rowkey is :{}", Bytes.toString(row));
			// String scanFilterName = Bytes.toString(rs.getValue(Bytes.toBytes("cf"),
			// Bytes.toBytes("name")));
			// log.info("scan filter name is :{}", scanFilterName);
		}
		HBaseUtils.closed(table);
		log.info(StringUtils.center("after", 18, "-"));
	}

	/**
	 * pageSize 表示每页的记录数,相当于SQL中的 limit 2
	 * 
	 * @throws Exception
	 */
	@Test
	public void pageFilter() throws Exception {
		PageFilter pageFilter = new PageFilter(2L);
		scan.setFilter(pageFilter);
	}

	/**
	 * 行键过滤器
	 */
	@Test
	public void rowFilter() throws Exception {
		Filter rowFilter = new RowFilter(CompareFilter.CompareOp.GREATER, new BinaryComparator(Bytes.toBytes("row3")));
		scan.setFilter(rowFilter);

	}

	// FamilyFilter familyFilter = new FamilyFilter(CompareFilter.CompareOp.EQUAL,
	// new BinaryComparator(Bytes.toBytes("cf")));
	/**
	 * MultiRowRangeFilter|多行范围过滤器
	 * <p>
	 * - startRow：起始行
	 * 
	 * - startRowInclusive：结果中是否包含起始行
	 * 
	 * - stopRow：结束行
	 * 
	 * - stopRowInclusive：结果中是否包含结束行
	 * </p>
	 * 
	 * @throws Exception
	 */
	@Test
	public void multiRowRangeFilter() throws Exception {
		// 构建从row1 到row2的RowRange
		RowRange rowRange1to2 = new RowRange("row1", true, "row2", true);
		// 构建从row5 到row7的RowRange
		RowRange rowRange5to7 = new RowRange("row5", true, "row7", true);
		// 构造RowRange的list
		List<RowRange> rowRanges = new ArrayList<RowRange>();
		// 添加这两个RowRage到list
		rowRanges.add(rowRange1to2);
		rowRanges.add(rowRange5to7);

		// 初始化 MultiRowRangeFilter
		Filter multiRowRangeFilter = new MultiRowRangeFilter(rowRanges);
		// 为scan设置filter
		scan.setFilter(multiRowRangeFilter);

	}

	/**
	 * <p>
	 * 前缀过滤器PrefixFilter 准确地说应该是行键前缀过滤器。这种过滤器可以根据行键的前缀匹配同样是个前缀的行。比如：要检索出所有行键以row起头的行
	 * <p>
	 * 
	 * @throws Exception
	 */
	@Test
	public void prefixFiter() throws Exception {
		Filter prefixFiter = new PrefixFilter(Bytes.toBytes("row"));
		scan.setFilter(prefixFiter);
	}
}
