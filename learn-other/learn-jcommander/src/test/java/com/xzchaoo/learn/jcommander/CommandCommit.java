package com.xzchaoo.learn.jcommander;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.List;

/**
 * @author xzchaoo
 * @date 2018/1/7
 */
@Parameters(commandNames = {"commit"}, separators = "=", commandDescription = "add")
public class CommandCommit {
	@Parameter(names = {"--amend"}, description = "amend")
	private boolean amend = false;
	@Parameter(names = {"-m"}, description = "commit message", hidden = false, required = false)
	private String msg;
	@Parameter(description = "files", required = false)
	private List<String> files;

	public boolean isAmend() {
		return amend;
	}

	public void setAmend(boolean amend) {
		this.amend = amend;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	@Override
	public String toString() {
		return "CommandCommit{" +
			"amend=" + amend +
			", msg='" + msg + '\'' +
			", files=" + files +
			'}';
	}
}
