package com.xzchaoo.learn.alg.twoditopn.mtselector.condition;

/**
 * 按照第一段是否直飞的情况来选择
 * created by zcxu at 2017/12/6
 *
 * @author zcxu
 */
public class FirstSegmentDirectUnit extends AbstractMaxCountUnit {
	public FirstSegmentDirectUnit(int maxCount) {
		super(maxCount);
	}

	@Override
	protected boolean canAdd(GroupAndPrice gap) {
		//TODO return 第一段是直飞
		return false;
	}
}
