package com.xzchaoo.learn.jcommander;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xzchaoo
 * @date 2018/1/7
 */
@Parameters(separators = "=")
public class Args {
	//@Parameter(required = true, description = "parameters")
//	@Parameter(description = "parameters")
	private List<String> parameters = new ArrayList<>();

	@Parameter(names = {"-v", "--verbose", "--log"}, description = "verbose mode")
	private boolean verbose;

	@Parameter(names = {"-debug"}, description = "debug mode")
	private boolean debug;

	@Parameter(names = {"-host"}, description = "host")
	private List<String> hosts = new ArrayList<>();

	//@Parameter(names = {"-password"}, description = "password", password = true)
	private String password = "";

	@Parameter(names = {"--help"}, help = true)
	private boolean help;

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public List<String> getHosts() {
		return hosts;
	}

	public void setHosts(List<String> hosts) {
		this.hosts = hosts;
	}

	@Override
	public String toString() {
		return "Args{" +
			"parameters=" + parameters +
			", verbose=" + verbose +
			", debug=" + debug +
			", hosts=" + hosts +
			", password='" + password + '\'' +
			'}';
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isHelp() {
		return help;
	}

	public void setHelp(boolean help) {
		this.help = help;
	}
}
