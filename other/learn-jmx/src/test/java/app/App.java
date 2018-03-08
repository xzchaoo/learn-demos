package app;

import com.alibaba.fastjson.JSON;

import org.hyperic.sigar.DiskUsage;
import org.hyperic.sigar.NetInfo;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.NetStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */
public class App {
	public static void main(String[] args) throws SigarException, InterruptedException {
		long mb = 1024 * 1024;
		MemoryMXBean m = ManagementFactory.getMemoryMXBean();
		MemoryUsage mu = m.getHeapMemoryUsage();
		System.out.println(String.format("heap init=%d used=%d max=%d committed=%d", mu.getInit() / mb, mu.getUsed() / mb, mu.getMax() / mb, mu.getCommitted() / mb));
		mu = m.getNonHeapMemoryUsage();
		System.out.println(String.format("non-heap init=%d used=%d max=%d committed=%d", mu.getInit() / mb, mu.getUsed() / mb, mu.getMax() / mb, mu.getCommitted() / mb));

		RuntimeMXBean r = ManagementFactory.getRuntimeMXBean();

		System.out.println(r.getName());//pid@机器名字
		System.out.println(r.getBootClassPath());
		System.out.println(r.getClassPath());
		System.out.println(r.getInputArguments());//启动参数
		System.out.println(r.getLibraryPath());
		System.out.println(r.getManagementSpecVersion());//版本号
		System.out.println(r.getSpecName());//jvm信息
		System.out.println(r.getVmName());//jvm信息
		System.out.println(r.getSpecVendor());//jvm信息
		System.out.println(r.getStartTime());//启动时间
		System.out.println(r.getVmVersion());//vm版本
		System.out.println(r.getUptime());
		//System.out.println(r.getSystemProperties()); 系统属性

		ThreadMXBean t = ManagementFactory.getThreadMXBean();
		System.out.println("ThreadCount=" + t.getThreadCount());
		System.out.println("DaemonThreadCount=" + t.getDaemonThreadCount());
		System.out.println("PeakThreadCount=" + t.getPeakThreadCount());
		System.out.println("TotalStartedThreadCount=" + t.getTotalStartedThreadCount());
		System.out.println(t.getCurrentThreadCpuTime());
		System.out.println(t.getCurrentThreadUserTime());
		ThreadInfo ti = t.getThreadInfo(t.getAllThreadIds()[0]);
		System.out.println(ti);
		System.out.println(ti.getThreadName() + " " + ti.getThreadState() + " " + ti.getWaitedTime() + " " + ti.getWaitedCount());

		CompilationMXBean c = ManagementFactory.getCompilationMXBean();
		System.out.println(c.getName());
		System.out.println(c.getTotalCompilationTime());

		ClassLoadingMXBean cl = ManagementFactory.getClassLoadingMXBean();
		System.out.println(cl.getLoadedClassCount());
		System.out.println(cl.getTotalLoadedClassCount());
		System.out.println(cl.getUnloadedClassCount());

		System.out.println(System.getProperty("java.version"));//判断JDK版本
		System.out.println(System.getProperty("sun.arch.data.model")); //判断是32位还是64位


		OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
		System.out.println(os.getName());
		System.out.println(os.getArch());
		System.out.println(os.getAvailableProcessors());
		System.out.println(os.getVersion());
		System.out.println(os.getSystemLoadAverage());

//		List<GarbageCollectorMXBean> gcs = ManagementFactory.getGarbageCollectorMXBeans();
//		for (GarbageCollectorMXBean gc : gcs) {
//			System.out.println();
//			System.out.println(gc.getName());
//			System.out.println(gc.getCollectionCount());
//			System.out.println(gc.getCollectionTime());
//			System.out.println(gc.getMemoryPoolNames()[0]);
//		}

		List<MemoryManagerMXBean> mms = ManagementFactory.getMemoryManagerMXBeans();
		for (MemoryManagerMXBean mm : mms) {
			System.out.println(mm.getName());
			System.out.println(Arrays.asList(mm.getMemoryPoolNames()));
		}

//		List<MemoryPoolMXBean> mps = ManagementFactory.getMemoryPoolMXBeans();
//		System.out.println("mps.size=" + mps.size());
//		for (MemoryPoolMXBean mp : mps) {
//			System.out.println(mp.getName() + " " + mp.getType());
//			System.out.println(mp.getUsage());
//			System.out.println(mp.getPeakUsage());
//			System.out.println();
//		}
		Runtime rt = Runtime.getRuntime();
		System.out.println(rt.freeMemory());
		System.out.println(rt.maxMemory());
		System.out.println(rt.totalMemory());

		//cpu核数
		System.out.println(rt.availableProcessors());

		Sigar s = new Sigar();
		System.out.println("NetInfo");
		NetInfo ni = s.getNetInfo();
		System.out.println("HostName=" + ni.getHostName());
		System.out.println("PrimaryDns=" + ni.getPrimaryDns());
		System.out.println("SecondaryDns=" + ni.getSecondaryDns());

//		String[] interfaceList = s.getNetInterfaceList();
//		System.out.println(Arrays.asList(interfaceList));
//		NetInterfaceConfig nic = s.getNetInterfaceConfig();
//		System.out.println(nic);
		System.out.println("网卡配置信息");
		//http://364434006.iteye.com/blog/1747490
		for (String nin : s.getNetInterfaceList()) {
			NetInterfaceConfig nic = s.getNetInterfaceConfig(nin);
			if ("59.78.22.103".equals(nic.getAddress())) {
				//if (!"0.0.0.0".equals(nic.getAddress()) && !"127.0.0.1".equals(nic.getAddress())) {
//			if ((nic.getFlags() & 1L) ==1L) {
				System.out.println(nic);
				NetInterfaceStat nis = s.getNetInterfaceStat(nin);
				System.out.println(nis);
//				print("RxPackets = " + ifstat.getRxPackets());// 接收的总包裹数
//				print("TxPackets = " + ifstat.getTxPackets());// 发送的总包裹数
//				print("RxBytes = " + ifstat.getRxBytes());// 接收到的总字节数
//				print("TxBytes = " + ifstat.getTxBytes());// 发送的总字节数
//				print("RxErrors = " + ifstat.getRxErrors());// 接收到的错误包数
//				print("TxErrors = " + ifstat.getTxErrors());// 发送数据包时的错误数
//				print("RxDropped = " + ifstat.getRxDropped());// 接收时丢弃的包数
//				print("TxDropped = " + ifstat.getTxDropped());// 发送时丢弃的包数
				//nis.getRxBytes()
			}
		}
		//比较有用
		NetStat ns = s.getNetStat();
		System.out.println(JSON.toJSONString(ns));

//		Tcp tcp = s.getTcp();
//		System.out.println(tcp);
//		for (FileSystem fs : s.getFileSystemList()) {
//			try {
//				System.out.println(fs);
//				FileSystemUsage fsu = s.getMountedFileSystemUsage(fs.getDevName());
//				System.out.println(fsu);
//
//			} catch (SigarException se) {
//
//			}
//		}
		long lastW = 0;
		long lastWC = 0;
		long lastR = 0;
		long lastRC = 0;
		for (int i = 0; i < 10; ++i) {
			DiskUsage du = s.getDiskUsage("C:/");
			System.out.println(du);
			//FileSystemUsage fsu = s.getFileSystemUsage("C:/");
			System.out.println("W " + (du.getWriteBytes() - lastW) / 1024);
			System.out.println("R " + (du.getReadBytes() - lastR) / 1024);
			System.out.println("WC " + (du.getWrites() - lastWC));
			System.out.println("RC " + (du.getReads() - lastRC));
			lastW = du.getWriteBytes();
			lastR = du.getReadBytes();
			lastWC = du.getWrites();
			lastRC = du.getReads();
			Thread.sleep(5000);
		}
		System.out.println(s.getProcList().length);

		//SigarNotImplementedException
		//System.out.println(s.getLoadAverage());

	}
}
