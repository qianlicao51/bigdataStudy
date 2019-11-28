package com.bigdata.hbase.booka.filter.admin;

import java.util.Collection;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;

import com.bigdata.hbase.booka.HBaseUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ManageFamily
 * @Description |列族管理
 * @Author 51
 * @Date 2019年11月27日 下午3:22:49
 */
@Slf4j
public class ManageFamily {
	/**
	 * <p>
	 * 通过设置数据生存时间(TimeToLive 缩写为TTL|单位是秒)， hbase可以自动清空过期的数据，避免数据库内数据过于庞大
	 * </p>
	 * 
	 * @param config
	 */
	public static void addColumnFamily(Configuration config) {
		try (Connection connection = ConnectionFactory.createConnection(config)) {
			Admin admin = connection.getAdmin();

			// 添加新的列族
			HColumnDescriptor newColumn = new HColumnDescriptor("newcf");
			// 设置TTL为10秒
			newColumn.setTimeToLive(10);
			admin.addColumn(TableName.valueOf("javatab"), newColumn);
		} catch (Exception e) {
		}
	}

	/**
	 * 关闭regions
	 * 
	 * @param config
	 */
	private static void closeRegion(Configuration config) {

		try (Connection connection = ConnectionFactory.createConnection(config)) {
			Admin admin = connection.getAdmin();
			// regionName |所在服务器+服务器端口+服务器启动码，中间用逗号分隔
			admin.closeRegion("javatab,,1574653804705.5f01e31f5261e2d046719f62e2dab5bd.", "quickstart.cloudera,60020,1574840930781");
		} catch (Exception e) {
		}
	}

	/**
	 * region重新上线
	 * 
	 * @param config
	 */
	public static void assignRegion(Configuration config) {

		try (Connection connection = ConnectionFactory.createConnection(config)) {
			Admin admin = connection.getAdmin();
			// regionName |所在服务器+服务器端口+服务器启动码，中间用逗号分隔
			admin.assign(Bytes.toBytes("javatab,,1574653804705.5f01e31f5261e2d046719f62e2dab5bd."));
		} catch (Exception e) {
		}
	}

	/**
	 * 如何查询RegionServer下的所有Region列表
	 * 
	 * @param config
	 */
	public static void getRegion(Configuration config) {

		try (Connection connection = ConnectionFactory.createConnection(config)) {
			Admin admin = connection.getAdmin();
			// 所在服务器+服务器端口服务器启动码
			ServerName sn = ServerName.valueOf("quickstart.cloudera:60020", 1574840930781L);
			List<HRegionInfo> regions = admin.getOnlineRegions(sn);
			for (HRegionInfo re : regions) {
				log.info("regionNmae is:{}", re.getRegionNameAsString());
			}
		} catch (Exception e) {
		}
	}

	public static void getRegionServer(Configuration config) {

		try (Connection connection = ConnectionFactory.createConnection(config)) {
			Admin admin = connection.getAdmin();
			Collection<ServerName> servers = admin.getClusterStatus().getServers();
			for (ServerName serverName : servers) {
				System.out.println("\nServer" + serverName.getServerName() + "拥有以下region");

				List<HRegionInfo> onlineRegions = admin.getOnlineRegions(serverName);
				for (HRegionInfo regionInfo : onlineRegions) {
					System.out.println(regionInfo.getRegionNameAsString());

				}
			}

		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {

		getRegionServer(HBaseUtils.conf);
	}
}
