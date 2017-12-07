package com.xzchaoo.learn.alg.twoditopn.mtselector.condition;

/**
 * 只处理 总航段数与直飞航段数的差值为某个固定值的情况
 * created by zcxu at 2017/12/6
 *
 * @author zcxu
 */
public class DirectSegmentCountMultiTicketSelectUnit extends AbstractMaxCountUnit {
	private final int difference;

	public DirectSegmentCountMultiTicketSelectUnit(int maxCount, int difference) {
		super(maxCount);
		this.difference = difference;
	}

	@Override
	protected boolean canAdd(GroupAndPrice gap) {
		return gap.getFlightCount() - gap.getSegmentCount() == difference;
	}
}
