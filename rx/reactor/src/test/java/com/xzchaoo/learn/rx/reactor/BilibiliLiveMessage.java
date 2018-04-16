package com.xzchaoo.learn.rx.reactor;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2017/6/6.
 */
public class BilibiliLiveMessage {
	private short h1;
	private short h2;
	private int type;
	private int h3;
	private JSONObject data;
	private byte[] bytes;
	private int onlineUserCount;

	public int getOnlineUserCount() {
		return onlineUserCount;
	}

	public void setOnlineUserCount(int onlineUserCount) {
		this.onlineUserCount = onlineUserCount;
	}

	public short getH1() {
		return h1;
	}

	public void setH1(short h1) {
		this.h1 = h1;
	}

	public short getH2() {
		return h2;
	}

	public void setH2(short h2) {
		this.h2 = h2;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getH3() {
		return h3;
	}

	public void setH3(int h3) {
		this.h3 = h3;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
