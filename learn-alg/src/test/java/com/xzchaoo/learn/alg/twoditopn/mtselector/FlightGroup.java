package com.xzchaoo.learn.alg.twoditopn.mtselector;

import com.xzchaoo.learn.alg.twoditopn.mtselector.condition.FlightPrice;

import java.util.List;

/**
 * created by zcxu at 2017/12/6
 *
 * @author zcxu
 */
public class FlightGroup {
	private List<FlightPrice> prices;

	public List<FlightPrice> getPrices() {
		return prices;
	}

	public void setPrices(List<FlightPrice> prices) {
		this.prices = prices;
	}
}
