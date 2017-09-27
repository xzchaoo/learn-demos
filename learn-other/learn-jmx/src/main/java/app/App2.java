package app;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.DiskUsage;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInfo;
import org.hyperic.sigar.NetInterfaceConfig;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/1/5.
 */
public class App2 {
	public static void main(String[] args) throws SigarException, InterruptedException, UnknownHostException {
		//http://364434006.iteye.com/blog/1747490
		Sigar sigar = new Sigar();

		//cpu
		CpuInfo[] cis = sigar.getCpuInfoList();
		System.out.println(String.format("一共有%d个cpu", cis.length));//有用
		System.out.println("cpu0=" + cis[0]);//有用
		System.out.println(cis[0].getCacheSize());
		Cpu c = sigar.getCpu();
		System.out.println("cpu=" + c);
		CpuPerc cp = sigar.getCpuPerc();
		System.out.println("CpuPerc=" + cp);//有用

		//mem
		Mem mem = sigar.getMem();
		System.out.println("mem=" + mem);
		//总内存
		System.out.println(mem.getTotal() / 1024.0 / 1024 / 1024);

		Swap s = sigar.getSwap();
		//页面交换

		//System.out.println(sigar.getFileSystemMap());
		FileSystemUsage fdu = sigar.getFileSystemUsage("C:/");
		System.out.println(fdu);

		OperatingSystem os = OperatingSystem.getInstance();
		String hostname = InetAddress.getLocalHost().getHostName();
		System.out.println(hostname);
		System.out.println(os);
		System.out.println(os.getCpuEndian());
		System.out.println(os.getDataModel());

		NetInfo ni = sigar.getNetInfo();
		System.out.println(ni.getHostName());
		System.out.println(ni.getDomainName());
//		System.out.println(Arrays.asList(sigar.getNetInterfaceList()));
//		for (String n : sigar.getNetInterfaceList()) {
//			NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(n);
//			if(cfg.getType().equals("Ethernet")){
//				if (!cfg.getAddress().equals("0.0.0.0")) {
//					System.out.println(cfg);
//				}
//			}
//		}
	}
}
