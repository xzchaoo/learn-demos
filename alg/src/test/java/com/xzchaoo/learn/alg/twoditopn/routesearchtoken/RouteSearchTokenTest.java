package com.xzchaoo.learn.alg.twoditopn.routesearchtoken;

import org.junit.Test;

/**
 * created by zcxu at 2017/12/7
 *
 * @author zcxu
 */
public class RouteSearchTokenTest {
	@Test
	public void test() {
		StringBuilder sb = new StringBuilder();
		//TODO java需要手动判断是否非null 否则会 插入 "null"

		//为null的请用空白字符串代替
		//格式= ${引擎}|${FlightKey}|${舱等信息}|${PriceFeature}|${PriceKey}|${TravelerEligibility}|${actualCityPair}
		//FlightKey=每个航班信息用-连接起来
		//航班信息=${FlightNo},${SegmentNo},${OperatingCarrier}-${TakeoffDateTime.Day}

		//舱等信息=SeatGrade,EncodeClass,BookingToken
		//EncodeClass只有NTF才有, 否则用${SeatGrade}代替
		//看了一下BookingToken好像经常是空白字符串

		//PriceFeature 0是普通 8是多票 其它的我能判断吗
		//PriceKey=${总售价},${总税},${PriceInfo[0].Agency.AgencyCode}-${PD.ValidatingCarrier}
		//actualCityPair 是什么? 通常是空白字符串

		//SharedPlatform|MU723,1,,16-MU5018,2,,18|Y,Y,-Y,Y,|0|430,309,TPYY-MU|NOR|
//		sb.append("SharedPlatform");
//		sb.append('|');
//		sb.append("FlightKey");
		sb.append((Object) null);
		String token = sb.toString();
		System.out.println(token);
	}
}
