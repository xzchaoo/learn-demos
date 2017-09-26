package com.xzchaoo.learn.hibernate.validator;


import org.hibernate.validator.constraints.ScriptAssert;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

//@ValidSearchRequest
@ScriptAssert(lang = "javascript", script = "_this.inboundDate==null || !_this.inboundDate.isBefore(_this.outboundDate)", message = "回程日期必须...",reportOn = "inboundDate")
public class SearchRequest extends BaseRequest {
	//Optional也可以验证

	//字段级别的验证 也可以放到getter方法上
	@NotEmpty //Default组进行验证
	@Size(min = 1, max = 2, groups = FooSearchGroup.class)//只在Foo的时候才验证
	@Size(min = 2, max = 3, groups = BarSearchGroup.class)//只在bar的时候才验证
	//这里还可以对内容内的元素进行验证
	private List<@NotNull @Size(min = 3, max = 4) String> departCityCodes;

	@NotEmpty
	@Size(min = 1, max = 2)
	private List<@NotNull @Size(min = 3, max = 4) String> arriveCityCodes;

	@NotNull
	@FutureOrPresent
	private LocalDate outboundDate;

	//TODO 可选参数 如果指定了那么一定要比 outboundDate大
	private LocalDate inboundDate;

	@NotNull
	@Valid //传递校验
	private FlightInfo flightInfo;

	//默认是secret
	@NotNull
	private Gender gender = Gender.SECRET;

	//正则表达式
//	@Pattern(regexp = "")
//	private String pattern;

	public List<String> getDepartCityCodes() {
		return departCityCodes;
	}

	public void setDepartCityCodes(List<String> departCityCodes) {
		this.departCityCodes = departCityCodes;
	}

	public List<String> getArriveCityCodes() {
		return arriveCityCodes;
	}

	public void setArriveCityCodes(List<String> arriveCityCodes) {
		this.arriveCityCodes = arriveCityCodes;
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

	public FlightInfo getFlightInfo() {
		return flightInfo;
	}

	public void setFlightInfo(FlightInfo flightInfo) {
		this.flightInfo = flightInfo;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
}
