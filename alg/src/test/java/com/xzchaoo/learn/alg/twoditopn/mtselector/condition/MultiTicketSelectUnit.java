package com.xzchaoo.learn.alg.twoditopn.mtselector.condition;

import java.util.List;

/**
 * 模仿AGG的比价单元 来对MT多票的结果进行选择
 * created by zcxu at 2017/12/6
 *
 * @author zcxu
 */
public interface MultiTicketSelectUnit {

	/**
	 * 这个比价单元是否已经超过个数限制了
	 *
	 * @return
	 */
	boolean isOvertake();

	/**
	 * 将元素加入比价单元
	 *
	 * @param group
	 */
	void add(GroupAndPrice group);

	/**
	 * 获得这个单元的选择结果
	 *
	 * @return
	 */
	List<GroupAndPrice> getSelectResult();
}
