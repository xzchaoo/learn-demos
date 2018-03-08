package com.xzchaoo.learn.alg.twoditopn.mtselector.condition;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zcxu at 2017/12/6
 *
 * @author zcxu
 */
public abstract class AbstractMultiTicketSelectUnit implements MultiTicketSelectUnit {
	/**
	 * 提供一个buffer 缓存合法的结果, 因为合法的结果不一定是最终需要的, 所以需要先buffer住
	 */
	protected final List<GroupAndPrice> buffer = new ArrayList<>();

	@Override
	public void add(GroupAndPrice gap) {
		if (canAdd(gap)) {
			buffer.add(gap);
		}
	}

	/**
	 * 判断是否可以加入buffer, 由子类实现具体逻辑
	 *
	 * @param gap
	 * @return
	 */
	protected abstract boolean canAdd(GroupAndPrice gap);

	@Override
	public List<GroupAndPrice> getSelectResult() {
		return buffer;
	}
}
