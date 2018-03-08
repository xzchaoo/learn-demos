package com.xzchaoo.learn.alg.twoditopn.mtselector.condition;

/**
 * 具备大小限制的的unit
 * created by zcxu at 2017/12/6
 *
 * @author zcxu
 */
public abstract class AbstractMaxCountUnit extends AbstractMultiTicketSelectUnit {
	/**
	 * 允许的最大个数
	 */
	private final int maxCount;

	/**
	 * 是否已经超过了最大个数
	 */
	private boolean overtake;

	public AbstractMaxCountUnit(int maxCount) {
		this.maxCount = maxCount;
	}

	@Override
	public boolean isOvertake() {
		return overtake;
	}

	@Override
	public void add(GroupAndPrice gap) {
		//已经超量就不加了
		if (overtake) {
			return;
		}
		if (canAdd(gap)) {
			//如果已经达到最大值了 就标记为超量 并且这个元素是不加入buffer的
			if (buffer.size() == maxCount) {
				overtake = true;
			} else {
				buffer.add(gap);
			}
		}
	}
}
