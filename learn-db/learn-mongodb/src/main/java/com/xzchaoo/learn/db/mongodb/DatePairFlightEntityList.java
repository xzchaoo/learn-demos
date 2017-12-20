package com.xzchaoo.learn.db.mongodb;

import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;
import java.util.List;

public class DatePairFlightEntityList {
	@Id
	private LocalDate outboundDate;
	private List<FlightEntity> entityList;

	public LocalDate getOutboundDate() {
		return outboundDate;
	}

	public void setOutboundDate(LocalDate outboundDate) {
		this.outboundDate = outboundDate;
	}

	public List<FlightEntity> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<FlightEntity> entityList) {
		this.entityList = entityList;
	}

	@Override
	public String toString() {
		return "DatePairFlightEntityList{" +
			"outboundDate=" + outboundDate +
			", entityList=" + entityList +
			'}';
	}
}
