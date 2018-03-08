package com.xzchaoo.learn.db.mongodb;


import com.mongodb.WriteResult;

import org.junit.Test;
import org.mongodb.morphia.aggregation.Group;
import org.mongodb.morphia.aggregation.Projection;
import org.mongodb.morphia.query.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FlightTest extends BaseMorphiaTest {
	private FlightEntity ow(boolean nonStop, String departureCityCode, String arrivalCityCode, LocalDate outboundDate, int salePrice, int totalPrice, int duration) {
		FlightEntity fe = new FlightEntity();
		fe.setTripType(TripType.OW);
		fe.setNonStop(nonStop);
		fe.setDepartureCityCode(departureCityCode);
		fe.setArrivalCityCode(arrivalCityCode);
		fe.setOutboundDate(outboundDate);
		fe.setInboundDate(null);
		fe.setSalePrice(salePrice);
		fe.setTotalPrice(totalPrice);
		fe.setDuration(duration);
		return fe;
	}

	private FlightEntity rt(boolean nonStop, String departureCityCode, String arrivalCityCode, LocalDate outboundDate, LocalDate inboundDate, int salePrice, int totalPrice, int duration) {
		FlightEntity fe = new FlightEntity();
		fe.setTripType(TripType.RT);
		fe.setNonStop(nonStop);
		fe.setDepartureCityCode(departureCityCode);
		fe.setArrivalCityCode(arrivalCityCode);
		fe.setOutboundDate(outboundDate);
		fe.setInboundDate(inboundDate);
		fe.setSalePrice(salePrice);
		fe.setTotalPrice(totalPrice);
		fe.setDuration(duration);
		return fe;
	}

	@Test
	public void test_batch_save() {
		WriteResult wr = datastore.delete(datastore.createQuery(FlightEntity.class));
		LOGGER.info("清空集合, count={}", wr.getN());
		List<FlightEntity> list = new ArrayList<>();
		for (int i = 0; i < 10; ++i) {
			list.add(ow(true, "SHA", "HKG", LocalDate.of(2017, 1, 1 + i), 100 + i, 110 + i, 60));
		}
		list.add(ow(false, "SHA", "HKG", LocalDate.of(2017, 1, 2), 100, 110, 80));
		list.add(ow(false, "BJS", "HKG", LocalDate.of(2017, 1, 2), 100, 110, 80));
		datastore.save(list);
	}

	/**
	 * 多城市 每天1个最低价
	 */
	@Test
	public void test_multiCityLP1() {
		LocalDate outboundDateBegin = LocalDate.of(2017, 1, 1);
		LocalDate outboundDateEnd = LocalDate.of(2017, 1, 30);
		List<String> departureCityCodes = Arrays.asList("SHA", "BJS");
		List<String> arrivalCityCodes = Arrays.asList("HKG");
		Iterator<IdDateFlightEntity> iter = datastore.createAggregation(FlightEntity.class)
			.match(
				datastore.createQuery(FlightEntity.class)
					.field("tripType").equal(TripType.OW)
					.field("departureCityCode").in(departureCityCodes)
					.field("arrivalCityCode").in(arrivalCityCodes)
					.field("outboundDate").greaterThanOrEq(outboundDateBegin)
					.field("outboundDate").lessThanOrEq(outboundDateEnd)
			)//.project(Projection.projection("price2", Projection.add("$salePrice", "$totalPrice")))
			.sort(Sort.ascending("salePrice"))
			.group("outboundDate", Group.grouping("entity", Group.first("$CURRENT")))
			.sort(Sort.ascending("_id"))
			.aggregate(IdDateFlightEntity.class);

		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
	}

	/**
	 * 多城市 每天n个最低价
	 */
	@Test
	public void test_multiCityLP2() {
		LocalDate outboundDateBegin = LocalDate.of(2017, 1, 1);
		LocalDate outboundDateEnd = LocalDate.of(2017, 1, 30);
		List<String> departureCityCodes = Arrays.asList("SHA", "BJS");
		List<String> arrivalCityCodes = Arrays.asList("HKG");
		int n = 2;
		Iterator<DatePairFlightEntityList> iter = datastore.createAggregation(FlightEntity.class)
			.match(
				datastore.createQuery(FlightEntity.class)
					.field("tripType").equal(TripType.OW)
					.field("departureCityCode").in(departureCityCodes)
					.field("arrivalCityCode").in(arrivalCityCodes)
					.field("outboundDate").greaterThanOrEq(outboundDateBegin)
					.field("outboundDate").lessThanOrEq(outboundDateEnd)
			)//.project(Projection.projection("price2", Projection.add("$salePrice", "$totalPrice")))
			.sort(Sort.ascending("salePrice"))
			.group("outboundDate", Group.grouping("entityList", Group.push("$CURRENT")))
			.project(Projection.projection("entityList", Projection.expression("$slice", "$entityList", 1)))
			.sort(Sort.ascending("_id"))
			.aggregate(DatePairFlightEntityList.class);

		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
	}

	/**
	 * 多城市  每天 每个城市对 一个最低价
	 */
	@Test
	public void test_multiCityLP3() {
		LocalDate outboundDateBegin = LocalDate.of(2017, 1, 1);
		LocalDate outboundDateEnd = LocalDate.of(2017, 1, 30);
		List<String> departureCityCodes = Arrays.asList("SHA", "BJS");
		List<String> arrivalCityCodes = Arrays.asList("HKG");

		Iterator<CityPairDatePairFlightEntity> iter = datastore.createAggregation(FlightEntity.class)
			.match(
				datastore.createQuery(FlightEntity.class)
					.field("tripType").equal(TripType.OW)
					.field("departureCityCode").in(departureCityCodes)
					.field("arrivalCityCode").in(arrivalCityCodes)
					.field("outboundDate").greaterThanOrEq(outboundDateBegin)
					.field("outboundDate").lessThanOrEq(outboundDateEnd)
			)
			.sort(Sort.ascending("salePrice"))
			.group(
				Arrays.asList(
					Group.grouping("departureCityCode"),
					Group.grouping("arrivalCityCode"),
					Group.grouping("outboundDate")
				),
				Group.grouping("entity", Group.first("$CURRENT"))
			)
			.aggregate(CityPairDatePairFlightEntity.class);
		while (iter.hasNext()) {
			CityPairDatePairFlightEntity e = iter.next();
			System.out.println(e);
		}
	}

	@Test
	public void test_multiCityLP4() {
		//多个城市对 指定日期
		//每个城市对的最低价格肯定保留(如果存在的话)
		//在此基础上再返回额外的50个最低价 (不考虑城市对) 不可重复

		//策略1. 先找出每个城市对的最低价, 假设有9个, 然后在找出 59 个最低价, 然后扣掉重复取最低50个价格, 这需要2次数据库查询操作

	}
}
