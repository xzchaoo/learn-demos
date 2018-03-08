package com.xzchaoo.learn.db.mongodb;

import org.mongodb.morphia.annotations.Id;

public class CityPairDatePairFlightEntity {
	@Id
	private CityPairDatePairFlightEntityId id;
	private FlightEntity entity;

	public CityPairDatePairFlightEntityId getId() {
		return id;
	}

	public void setId(CityPairDatePairFlightEntityId id) {
		this.id = id;
	}

	public FlightEntity getEntity() {
		return entity;
	}

	public void setEntity(FlightEntity entity) {
		this.entity = entity;
	}

	@Override
	public String toString() {
		return "CityPairDatePairFlightEntity{" +
			"id=" + id +
			", entity=" + entity +
			'}';
	}

}
