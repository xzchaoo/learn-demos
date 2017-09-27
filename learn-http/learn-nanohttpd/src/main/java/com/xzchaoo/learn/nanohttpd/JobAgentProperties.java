package com.xzchaoo.learn.nanohttpd;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Administrator on 2017/4/25.
 */
@ConfigurationProperties(prefix = "job.agent")
public class JobAgentProperties {
	private String hostname = "0.0.0.0";
	private int port = 7739;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
}
