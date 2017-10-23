package com.xzchaoo.learn.db.mongodb;

import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;

public class IdDateFlightEntity {
	@Id
	private LocalDate id;
	private FlightEntity entity;

	public LocalDate getId() {
		return id;
	}

	public void setId(LocalDate id) {
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
		return "IdDateFlightEntity{" +
			"id=" + id +
			", entity=" + entity +
			'}';
	}
}
