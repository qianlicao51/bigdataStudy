package com.bigdata.hbase.booka.filter.row;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FuzzyRowFilter;
import org.apache.hadoop.hbase.filter.InclusiveStopFilter;
import org.apache.hadoop.hbase.filter.MultiRowRangeFilter;
import org.apache.hadoop.hbase.filter.MultiRowRangeFilter.RowRange;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RandomRowFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;
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
		re.close();
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

	//
	/**
	 * 模糊行键过滤器
	 * <p>
	 * - 行键：输入你需要匹配的行键关键字。对于那些需要匹配的字符所在的位置，可以用任意字符。
	 * -行键掩码：行键掩码的长度必须和行键长度一样，在需要磨合匹配的字符处标为1，其他位置标记上0。
	 * <p>
	 * 
	 * @throws Exception
	 */
	@Test
	public void fuzzyRowFilter() throws Exception {
		FuzzyRowFilter fuzzyRowFilter = new FuzzyRowFilter(Arrays.asList(//
				new Pair<byte[], byte[]>(Bytes.toBytesBinary("2016_??_??123"), //
						new byte[] { 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 0, })));
		scan.setFilter(fuzzyRowFilter);
	}

	/**
	 * 包含结尾过滤器
	 * <p>
	 * 当我们使用scan来扫描数据的时候，如果使用STOPROW来指定终止行，结果集并不包含终止行。如果想在结果中包含终止行可以有两种方式。 <br>
	 * 1.在终止行的rowkey上增加一个字节的数组，然后把增加了一个字节的rowkey作为STOPROW<br>
	 * 2.使用包含结尾过滤器<font color='blue'>InclusiveStopFilter</font><br>
	 * <p>
	 * 
	 * @throws Exception
	 */
	@Test
	public void stopFilter() throws Exception {
		scan = new Scan(Bytes.toBytes("row8"));

		InclusiveStopFilter stopFilter = new InclusiveStopFilter(Bytes.toBytes("row9"));
		scan.setFilter(stopFilter);

	}

	/**
	 * <p>
	 * chance是一个随机比较的值。当扫描遍历数据的时候，每遍历到一行数据，hbase就会调用Random.nextFloat()来得出一个随机数，<br>
	 * 并用这个随机数跟你提供的change来进行比较，如果比较的结果是随机数比chance小，则该条记录就会被选择出来，反之会被过滤掉。<br>
	 * chance的取值范围是0.0到0.1，如果设置为负数，那么所有的结果都会被过滤掉，如果设定的比1.0大，那么包含所有的行。<br>
	 * 所有，可以把chance看成是你要选取的数据在整个表中数据的百分比
	 * <p>
	 * 
	 * @throws Exception
	 */
	@Test
	public void randomRowFilter() throws Exception {
		// 初始化随机行过滤器
		RandomRowFilter randomRowFilter = new RandomRowFilter(new Float(0.5));
		scan.setFilter(randomRowFilter);

	}
}
