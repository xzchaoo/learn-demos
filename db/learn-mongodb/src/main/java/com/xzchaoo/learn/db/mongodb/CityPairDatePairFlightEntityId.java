package com.xzchaoo.learn.db.mongodb;

import java.time.LocalDate;

public class CityPairDatePairFlightEntityId {
	private String departureCityCode;
	private String arrivalCityCode;
	private LocalDate outboundDate;
	private LocalDate inboundDate;

	public String getDepartureCityCode() {
		return departureCityCode;
	}

	public void setDepartureCityCode(String departureCityCode) {
		this.departureCityCode = departureCityCode;
	}

	public String getArrivalCityCode() {
		return arrivalCityCode;
	}

	public void setArrivalCityCode(String arrivalCityCode) {
		this.arrivalCityCode = arrivalCityCode;
	}

	public LocalDate getOutboundDate() {
		return outboundDate;
	}

	public void setOutboundDate(LocalDate outboundDate) {
		this.outboundDate = outboundDate;
	}

	public LocalDate getInboundDate() {
		return inboundDate;
	}

	public void setInboundDate(LocalDate inboundDate) {
		this.inboundDate = inboundDate;
	}

	@Override
	public String toString() {
		return "CityPairDatePairFlightEntityId{" +
			"departureCityCode='" + departureCityCode + '\'' +
			", arrivalCityCode='" + arrivalCityCode + '\'' +
			", outboundDate=" + outboundDate +
			", inboundDate=" + inboundDate +
			'}';
	}
}
