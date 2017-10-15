package com.xzchaoo.learn.db.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.jdbc.Driver;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 选取开源社区最常用的数据库连接池 做几个简单的demo, 数据库连接池的用法大部分是一样的, 参数的意义大部分也一样, 这个测试会解释一些常见参数的意义
 * 数据库连接池 HikariCP tomcat-jdbc Druid 目前这几个连接池的名声比较好就测这几个了
 * TODO 有个问题 动态调整数据库连接池的大小之后似乎不能马上生效 要等到每个连接都超过其 maxLifetime 这可能是30分钟
 */
public class DataSourceTest {
	Properties props = new Properties();

	{
		props.setProperty("cachePrepStmts", "true");
		props.setProperty("prepStmtCacheSize", "250");
		props.setProperty("prepStmtCacheSqlLimit", "2048");
		props.setProperty("useServerPrepStmts", "true");
		props.setProperty("useLocalSessionState", "true");
		props.setProperty("useLocalTransactionState", "true");
		props.setProperty("rewriteBatchedStatements", "true");
		props.setProperty("cacheResultSetMetadata", "true");
		props.setProperty("cacheServerConfiguration", "true");
		props.setProperty("elideSetAutoCommits", "true");
		props.setProperty("maintainTimeStats", "false");
	}

	@Test
	public void test_hikaricp() throws InterruptedException {
		//HikariConfig cfg = new HikariConfig();
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setAllowPoolSuspension(true);
		dataSource.setDriverClassName(Driver.class.getName());
		dataSource.setJdbcUrl("jdbc:mysql://202.120.57.151:3306/test");
		dataSource.setUsername("root");
		dataSource.setPassword("70862045");

		//如果30秒还拿不到一个连接就异常
		//dataSource.setConnectionTimeout(30000);
		//仅当 minimumIdle < maximumPoolSize 时有效
		//只有空闲超过这个时间 才会被移除
		dataSource.setMinimumIdle(8);
		dataSource.setMaximumPoolSize(8);
		//dataSource.setScheduledExecutor();
		//空闲和关闭的时候会检查
		dataSource.setIdleTimeout(15000);
		dataSource.setConnectionTestQuery("select 1");
		dataSource.setValidationTimeout(2000);
		dataSource.setScheduledExecutor(Executors.newScheduledThreadPool(4));
		dataSource.setMaxLifetime(180000);
		//minimumIdle 推荐设置成和最大值一样的
		//最大连接数 包含了空闲的 和正在使用的
		//当一个请求要获得连接的时候 如果没有空闲连接 就会阻塞直到有或者connectionTimeout

		//mysql推荐
		dataSource.setDataSourceProperties(props);
		AtomicInteger ai = new AtomicInteger();
		ExecutorService es = Executors.newFixedThreadPool(16);
		for (int i = 0; i < 16; ++i) {
			es.execute(() -> {
				for (int j = 0; j < 1000; ++j) {
					try {
						Connection c = dataSource.getConnection();
						Statement s = c.createStatement();
						ResultSet rs = s.executeQuery("select 1");
						rs.next();
						rs.close();
						s.close();
						c.close();
						int cc = ai.incrementAndGet();
						if (cc % 1000 == 0) {
							System.out.println(cc);
						}
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		new Thread(() -> {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("设置成2");
			dataSource.setMaximumPoolSize(2);
			dataSource.setMinimumIdle(2);
		}).start();
		es.shutdown();
		es.awaitTermination(10, TimeUnit.MINUTES);
		System.out.println("执行完了");
		Thread.sleep(1000000);
	}

	@Test
	public void test_druid() throws Exception {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(Driver.class.getName());
		dataSource.setUrl("jdbc:mysql://202.120.57.151:3306/test");
		dataSource.setUsername("root");
		dataSource.setPassword("70862045");
		dataSource.setMaxActive(8);
		dataSource.setMinIdle(8);
		dataSource.setInitialSize(8);
		dataSource.setConnectProperties(props);
		AtomicInteger ai = new AtomicInteger();
		ExecutorService es = Executors.newFixedThreadPool(16);
		for (int i = 0; i < 16; ++i) {
			es.execute(() -> {
				for (int j = 0; j < 100; ++j) {
					try {
						Connection c = dataSource.getConnection();
						Statement s = c.createStatement();
						ResultSet rs = s.executeQuery("select 1");
						rs.next();
						rs.close();
						s.close();
						c.close();
						int cc = ai.incrementAndGet();
						if (cc % 100 == 0) {
							System.out.println(cc);
						}
						Thread.sleep(10);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		new Thread(() -> {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("设置成2");
			dataSource.setMinIdle(2);
			dataSource.setMaxActive(2);
			//dataSource.setMaxActive(2);
			//dataSource.setMaxIdle(2);
			//dataSource.setMinIdle(2);
			//dataSource.setInitialSize(2);
		}).start();
		es.shutdown();
		es.awaitTermination(10, TimeUnit.MINUTES);
		System.out.println("执行完了");
		Thread.sleep(1000000);
	}

	@Test
	public void test_tomcat_jdbc() throws Exception {
		//http://blog.csdn.net/white_ice/article/details/52610136
		DataSource dataSource = new DataSource();
		dataSource.setDriverClassName(Driver.class.getName());
		dataSource.setUrl("jdbc:mysql://202.120.57.151:3306/test");
		dataSource.setUsername("root");
		dataSource.setPassword("70862045");

		//初始化4个连接 最小应该也是4个连接
		dataSource.setInitialSize(8);

		//最多有16个空闲
		dataSource.setMinIdle(8);
		dataSource.setMaxIdle(8);
		//最多开16个连接
		dataSource.setMaxActive(8);

		//借出连接的时候不检查
		//dataSource.setTestOnBorrow(false);

		//验证的SQL语句
		//dataSource.setValidationQuery("select 1");
		//2秒算超时
		//dataSource.setValidationQueryTimeout(2000);
		//每3秒应该验证一次
		//dataSource.setValidationInterval(3000);

		//空闲的时候进行验证
		//dataSource.setTestWhileIdle(true);
		//检查清除线程的间隔时间
		//dataSource.setTimeBetweenEvictionRunsMillis(5000);
		//空闲超过60秒才具备被清理的资格
		//dataSource.setMinEvictableIdleTimeMillis(60000);

		//当一个连接借出去超过 一定时间还没有返回 就认为他是 abandoned 会移除它
		//dataSource.setRemoveAbandoned(true);
		//dataSource.setRemoveAbandonedTimeout(300000);

		//最大年龄 当连接返回池的时候会 检查该连接的年龄 超过这个值的话就会真的关闭它
		//dataSource.setMaxAge(30000);

		//dataSource.setMaxWait(30000);

		//是否公平
		//dataSource.setFairQueue(true);
		dataSource.setDbProperties(props);
		AtomicInteger ai = new AtomicInteger();
		ExecutorService es = Executors.newFixedThreadPool(16);
		for (int i = 0; i < 16; ++i) {
			es.execute(() -> {
				for (int j = 0; j < 50; ++j) {
					try {
						Connection c = dataSource.getConnection();
						Statement s = c.createStatement();
						ResultSet rs = s.executeQuery("select 1");
						rs.next();
						rs.close();
						s.close();
						c.close();
						int cc = ai.incrementAndGet();
						if (cc % 1000 == 0) {
							System.out.println(cc);
						}
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		new Thread(() -> {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("设置成2");
			//dataSource.setMaxActive(2);
			//dataSource.setMaxIdle(2);
			//dataSource.setMinIdle(2);
			//dataSource.setInitialSize(2);
		}).start();
		es.shutdown();
		es.awaitTermination(10, TimeUnit.MINUTES);
		System.out.println("执行完了");
		Thread.sleep(1000000);
	}
}
