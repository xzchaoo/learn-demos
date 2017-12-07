package com.xzchaoo.learn.alg.twoditopn.mtselector.condition;

import java.util.HashSet;
import java.util.Set;

/**
 * 按照第1个segment的直飞情况来保留选择, 同一个航班组合只考虑一次
 * created by zcxu at 2017/12/6
 *
 * @author zcxu
 */
public class UniqueFirstSegmentUnit extends AbstractMaxCountUnit {
	//TODO 其实在数量小的情况下, 用数组代替Set效率更高, redis的实现就是这样 需要看下C#是如何实现的额
	//TODO 经过测试 上面的优化似乎是无效的 吐血了... 可以省一些内存倒是真的
	private Set<String> processedGroupIdSet;

	public UniqueFirstSegmentUnit(int maxCount) {
		super(maxCount);
		//TODO 这里有一个小优化就是先初始化容器的大小 但是java的HashSet 在满75% 时就会开始进行扩容, 后面这个值也不一定会是75%, 总之这里有一个可以优化的点
		processedGroupIdSet = new HashSet<>(maxCount * 4 / 3);
	}

	@Override
	protected boolean canAdd(GroupAndPrice gap) {
		//TODO 第一段是直飞 && groupId 第一次出现
		//只考虑第一程的航班号
		return false;
	}
}
