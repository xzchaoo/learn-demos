package com.xzchaoo.learn.alg.twoditopn.mtselector.condition;

import com.xzchaoo.learn.alg.twoditopn.mtselector.FlightGroup;

/**
 * 封装一个价格, 航组, 以及一些id, 用于唯一的标识一个组或价格
 * created by zcxu at 2017/12/6
 *
 * @author zcxu
 */
public class GroupAndPrice {
	private final int groupId;
	private final FlightGroup group;
	//TODO 我如何知道 原始的segmentCount, 通过取最后一个的 SegmentNo行吗
	private final int segmentCount;
	private final int flightCount;
	private final int priceId;
	private final FlightPrice price;
	//根据需要 这里可能还需要一些其它的变量, 不过可以用lazy运算

	public GroupAndPrice(int groupId, FlightGroup group, int segmentCount, int flightCount, int priceId, FlightPrice price) {
		this.groupId = groupId;
		this.group = group;
		this.segmentCount = segmentCount;
		this.flightCount = flightCount;
		this.priceId = groupId;
		this.price = price;
	}

	public int getGroupId() {
		return groupId;
	}

	public FlightGroup getGroup() {
		return group;
	}

	public int getSegmentCount() {
		return segmentCount;
	}

	public int getFlightCount() {
		return flightCount;
	}

	public int getPriceID() {
		return priceId;
	}

	public FlightPrice getPrice() {
		return price;
	}
}
