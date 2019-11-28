package com.bigdata.hbase.booka.filter.family;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.ColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.filter.MultipleColumnPrefixFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bigdata.hbase.booka.HBaseUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName FamilyFilterDemo
 * @Description |列过滤器
 * @Author 51
 * @Date 2019年11月27日 上午8:33:10
 */
@Slf4j
public class FamilyFilterDemo {
	Table table;
	Scan scan;

	@Before
	public void runBeforeTestMethod() throws IOException {
		table = HBaseUtils.getTale("javatab");
		scan = new Scan();
		System.out.println(table.getTableDescriptor());
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
	 * 列族过滤器<br>
	 * 执行这个过滤器后，只有列族为cf的列会被加入结果集
	 * 
	 * @throws Exception
	 */
	@Test
	public void familyFilter() throws Exception {
		FamilyFilter familyFilter = new FamilyFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("cf")));
		scan.setFilter(familyFilter);
	}

	/**
	 * 列过滤器 <br>
	 * 针对列名进行过滤的过滤器
	 */
	@Test
	public void qualifilter() throws Exception {
		Filter qualifierFilter = new QualifierFilter(CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("name")));
		scan.setFilter(qualifierFilter);

	}

	/**
	 * 列前缀过滤器|专门针对行键的前缀进行过 滤的
	 * 
	 * @throws Exception
	 */
	@Test
	public void columnPrefixFilter() throws Exception {
		ColumnPrefixFilter columnPrefixFilter = new ColumnPrefixFilter(Bytes.toBytes("c"));
		scan.setFilter(columnPrefixFilter);
	}

	/**
	 * 多列前缀过滤器
	 * 
	 * @throws Exception
	 */
	@Test
	public void multipleColumnPrefixFilter() throws Exception {

		byte[][] filter_prefix = new byte[2][];

		filter_prefix[0] = Bytes.toBytes("ci");
		filter_prefix[1] = Bytes.toBytes("cf");

		Filter multipleColumnPrefixFilter = new MultipleColumnPrefixFilter(filter_prefix);

		scan.setFilter(multipleColumnPrefixFilter);

	}

	@Test
	public void keyOnlyFilter() throws Exception {

		KeyOnlyFilter keyOnlyFilter = new KeyOnlyFilter();

		table = HBaseUtils.getTale("hb_sysdic");
		scan.setFilter(keyOnlyFilter);

		ResultScanner rs = table.getScanner(scan);
		printResult(rs);
		rs.close();
	}

	/**
	 * 首次列键过滤器
	 * <p>
	 * 传统数据库中经常使用cout操作来做行数统计。可是hbase中，必须遍历所有的数据才能知道总共多少行，这得多慢啊。
	 * 如果可以只检索到一个列就立马调到下一行就好了。所以产生了这种过滤器，这种过滤器在做行统计时的速度非常快
	 * </p>
	 * 
	 * @throws Exception
	 */
	@Test
public void count() throws Exception {

	FirstKeyOnlyFilter firstKeyOnlyFilter = new FirstKeyOnlyFilter();

	table = HBaseUtils.getTale("hb_sysdic");
	scan.setFilter(firstKeyOnlyFilter);
	ResultScanner rs = table.getScanner(scan);
	int count = 0;
	for (Result result : rs) {
		count++;
	}
	rs.close();
	System.out.println("共计+" + count + "行");
}

	/**
	 * @param rs
	 */
	private void printResult(ResultScanner rs) {
		for (Result r : rs) {
			List<Cell> cells = r.listCells();

			ArrayList<String> sb = new ArrayList<String>();

			byte[] row = r.getRow();

			sb.add("row =" + Bytes.toString(row));
			for (Cell cell : cells) {
				sb.add("column=" + new String(CellUtil.cloneQualifier(cell)));
			}
			System.out.println(StringUtils.join(sb, ", "));
		}
	}
}
