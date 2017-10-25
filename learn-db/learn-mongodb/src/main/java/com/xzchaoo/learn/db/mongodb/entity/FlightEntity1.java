package com.xzchaoo.learn.db.mongodb.entity;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.utils.IndexType;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * TODO 解决实体共用问题
 * TODO nonStrop 需要放在索引里?
 * TODO 实体有哪些属性可能需要重新考虑一下
 * TODO 将status放在复合索引的第一个位置会不会导致效率下降?
 *
 * @author zcxu
 */
@Entity(noClassnameStored = true)
//TODO 索引待完善
@Indexes({
    //for 标记删除 和 删除
    @Index(fields = {
        @Field("status"),
        @Field("tripType"),
        @Field("nonStop"),
        @Field("engine"),
        @Field("departureCityCode"),
        @Field("arrivalCityCode"),
        @Field("outboundDate"),
        @Field("inboundDate")
    }),

    //for查询
    @Index(fields = {
        @Field("status"),
        @Field("tripType"),
        @Field("nonStop"),
        @Field("departureCityCode"),
        @Field("arrivalCityCode"),
        @Field("outboundDate"),
        @Field("inboundDate"),
        @Field("salePrice")
    }),

    //for查询
    @Index(fields = {
        @Field("status"),
        @Field("tripType"),
        @Field("nonStop"),
        @Field("departureCityCode"),
        @Field("arrivalCityCode"),
        @Field("outboundDate"),
        @Field("inboundDate"),
        @Field("totalPrice")
    }),

    //for查询
    @Index(fields = {
        @Field("status"),
        @Field("tripType"),
        @Field("nonStop"),
        @Field("departureCityCode"),
        @Field("arrivalCityCode"),
        @Field("outboundDate"),
        @Field("inboundDate"),
        @Field("duration")
    }),
    @Index(fields = {@Field(value = "sourceType", type = IndexType.HASHED)})
})
public class FlightEntity1 {
    @Id
    private ObjectId id;
    private String status;
    private String tripType;
    private boolean nonStop;
    private String engine;
    private String departureCityCode;
    private String arrivalCityCode;
    private LocalDate outboundDate;
    private LocalDate inboundDate;

    private int salePrice;
    private int totalPrice;
    private int duration;

    private int distance;
    private int departureCountryId;
    private int arrivalCountryId;

    private byte[] data;

    private String sourceType;

    @Indexed(options = @IndexOptions())
    private LocalDateTime createdTime;

    @Indexed(options = @IndexOptions())
    private LocalDateTime updatedTime;

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getDepartureCountryId() {
        return departureCountryId;
    }

    public void setDepartureCountryId(int departureCountryId) {
        this.departureCountryId = departureCountryId;
    }

    public String getDepartureCityCode() {
        return departureCityCode;
    }

    public void setDepartureCityCode(String departureCityCode) {
        this.departureCityCode = departureCityCode;
    }

    public int getArrivalCountryId() {
        return arrivalCountryId;
    }

    public void setArrivalCountryId(int arrivalCountryId) {
        this.arrivalCountryId = arrivalCountryId;
    }

    public String getArrivalCityCode() {
        return arrivalCityCode;
    }

    public void setArrivalCityCode(String arrivalCityCode) {
        this.arrivalCityCode = arrivalCityCode;
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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

    public boolean isNonStop() {
        return nonStop;
    }

    public void setNonStop(boolean nonStop) {
        this.nonStop = nonStop;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    @PrePersist
    public void prePersist() {
        this.createdTime = this.createdTime == null ? LocalDateTime.now() : this.createdTime;
        this.updatedTime = this.updatedTime == null ? this.createdTime : LocalDateTime.now();
    }

}
