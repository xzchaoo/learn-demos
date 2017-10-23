package com.xzchaoo.learn.db.mongodb;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.PrePersist;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Indexes({@Index(fields = {
	@Field("tripType"),
	@Field("nonStop"),
	@Field("departureCityCode"),
	@Field("arrivalCityCode"),
	@Field("outboundDate"),
	@Field("inboundDate"),
	@Field("salePrice"),
}), @Index(fields = {
	@Field("tripType"),
	@Field("nonStop"),
	@Field("departureCityCode"),
	@Field("arrivalCityCode"),
	@Field("outboundDate"),
	@Field("inboundDate"),
	@Field("totalPrice"),
}), @Index(fields = {
	@Field("tripType"),
	@Field("nonStop"),
	@Field("departureCityCode"),
	@Field("arrivalCityCode"),
	@Field("outboundDate"),
	@Field("inboundDate"),
	@Field("duration"),
})
})
public class FlightEntity {
	@Id
	private ObjectId id;
	private TripType tripType;
	private boolean nonStop;
	private String departureCityCode;
	private String arrivalCityCode;
	private LocalDate outboundDate;
	private LocalDate inboundDate;
	private int salePrice;
	private int totalPrice;
	private int duration;
	private LocalDateTime createdTime;

	@PrePersist
	public void prePersist() {
		this.createdTime = this.createdTime == null ? LocalDateTime.now() : this.createdTime;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public TripType getTripType() {
		return tripType;
	}

	public void setTripType(TripType tripType) {
		this.tripType = tripType;
	}

	public boolean isNonStop() {
		return nonStop;
	}

	public void setNonStop(boolean nonStop) {
		this.nonStop = nonStop;
	}

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

	public int getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "FlightEntity{" +
			"id=" + id +
			", tripType=" + tripType +
			", nonStop=" + nonStop +
			", departureCityCode='" + departureCityCode + '\'' +
			", arrivalCityCode='" + arrivalCityCode + '\'' +
			", outboundDate=" + outboundDate +
			", inboundDate=" + inboundDate +
			", salePrice=" + salePrice +
			", totalPrice=" + totalPrice +
			", duration=" + duration +
			'}';
	}
}
