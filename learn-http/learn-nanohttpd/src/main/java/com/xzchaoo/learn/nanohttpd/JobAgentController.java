package com.xzchaoo.learn.nanohttpd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.OperatingSystem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

import static com.xzchaoo.learn.nanohttpd.RespUtils.ok;

/**
 * Created by Administrator on 2017/4/24.
 */
@Component
public class JobAgentController {
	private static final Sigar sigar = new Sigar();
	private static final long MB = 1024 * 1024;

	public NanoHTTPD.Response getPerformance(NanoHTTPD.IHTTPSession session) {
		JSONObject r = new JSONObject();
		r.put("code", 0);
		r.put("msg", "OK");

		JSONObject data = new JSONObject();
		try {
			JSONObject memr = new JSONObject();
			Mem mem = sigar.getMem();
			memr.put("free", mem.getFree() / MB);
			memr.put("total", mem.getTotal() / MB);
			data.put("mem", memr);

			CpuPerc cpuPerc = sigar.getCpuPerc();
			JSONObject cpur = new JSONObject();
			cpur.put("user", (int) (cpuPerc.getUser() * 100));
			cpur.put("sys", (int) (cpuPerc.getSys() * 100));
			cpur.put("idle", (int) (cpuPerc.getIdle() * 100));
			CpuInfo[] cpuInfos = sigar.getCpuInfoList();
			cpur.put("count", cpuInfos.length);
			CpuInfo cpuInfo = sigar.getCpuInfoList()[0];
			cpur.put("model", cpuInfo.getModel());
			cpur.put("mhz", cpuInfo.getMhz());
			cpur.put("vendor", cpuInfo.getVendor());
			data.put("cpu", cpur);
//
//			JSONArray cpus = new JSONArray();
//			Map<String, int[]> map = new HashMap<>();
//			Map<String, Integer> mhz2 = new HashMap<>();
//			Map<String, String> vendor2 = new HashMap<>();
//			for (CpuInfo ci : cpuInfos) {
//				String model = ci.getModel();
//				++map.computeIfAbsent(model, key -> new int[]{0})[0];
//				mhz2.put(model, ci.getMhz());
//				vendor2.put(model, ci.getVendor());
//			}
//			//JSONObject cpu1 = new JSONObject();
//			for (String model : map.keySet()) {
//				int count = map.get(model)[0];
//				JSONObject ja = new JSONObject();
//				ja.put("count",count);
//				ja.put("model",model);
//				ja.put("vendor",vendor2.get(model));
//				ja.put("mhz",mhz2.get(model));
//				cpus.add(ja);
//			}
//
//			data.put("cpus", cpus);

			try {
				data.put("loadAverage", sigar.getLoadAverage());
			} catch (Exception e) {

			}

			OperatingSystemMXBean osb = ManagementFactory.getOperatingSystemMXBean();
			JSONObject osr = new JSONObject();
			osr.put("systemLoadAverage", osb.getSystemLoadAverage());

			OperatingSystem os = OperatingSystem.getInstance();
			osr.put("name", os.getName());
			osr.put("arch", os.getArch());
			osr.put("version", os.getVersion());
			osr.put("vendorName", os.getVendorName());
			osr.put("vendor", os.getVendor());
			osr.put("vendorVersion", os.getVendorVersion());
			osr.put("description", os.getDescription());
			data.put("os", osr);


			//网络特别耗时
//			JSONObject netr = new JSONObject();
//			NetInfo ni = sigar.getNetInfo();
//			netr.put("hostname", ni.getHostName());
//			netr.put("primaryDns", ni.getPrimaryDns());
//			netr.put("secondaryDns", ni.getSecondaryDns());
//			data.put("net", netr);

//			System.out.println(sigar.getUptime().getUptime());//单位是秒

		} catch (SigarException e) {
			e.printStackTrace();
		}
		r.put("data", data);

		return ok(session, r);
	}

	public NanoHTTPD.Response executor(NanoHTTPD.IHTTPSession session) {
		JSONObject r = new JSONObject();
		try {
			Map<String, String> files = new HashMap<>();
			session.parseBody(files);
			String content = files.get("postData");
			r = JSON.parseObject(content);
			System.out.println(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ok(r);
	}
}
