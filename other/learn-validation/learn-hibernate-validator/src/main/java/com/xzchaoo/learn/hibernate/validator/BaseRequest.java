package com.xzchaoo.learn.hibernate.validator;

import javax.validation.constraints.Min;

//类级别的验证
//验证是可以被继承的
public class BaseRequest {
	//可以被子类继承
	@Min(value = 1, message = "requestId必须制定")
	@ModNConstraint(divider = 3, remain = 2)
	private int requestId;

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
}
