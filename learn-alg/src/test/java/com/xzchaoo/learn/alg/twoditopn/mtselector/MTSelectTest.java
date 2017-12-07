package com.xzchaoo.learn.alg.twoditopn.mtselector;

import com.xzchaoo.learn.alg.twoditopn.mtselector.condition.FlightPrice;
import com.xzchaoo.learn.alg.twoditopn.mtselector.condition.GroupAndPrice;
import com.xzchaoo.learn.alg.twoditopn.mtselector.condition.MultiTicketSelectUnit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 从多个MT航班里跳出最优航班
 * 会提供多个条件, 只要满足任意一个条件就可以保留, 保留可能是保留全部, 也可能是保留所有满足条件的最小的N个
 * created by zcxu at 2017/12/6
 *
 * @author zcxu
 */
public class MTSelectTest {
	@Test
	public void test() {
		List<FlightGroup> gs = new ArrayList<>();

		//1. 将gs分裂成多个gaps, 并初始化一些id
		List<GroupAndPrice> gaps = new ArrayList<>();
		int groupId = 0;
		int priceId = 0;
		//这里还是先假设gaps的大小规模和gs差不多
		for (FlightGroup g : gs) {
			int segmentCount = 1;
			int flightCount = 1;
			for (FlightPrice p : g.getPrices()) {
				gaps.add(new GroupAndPrice(groupId, g, segmentCount, flightCount, groupId++, p));
			}
			++groupId;
		}

		//2. 按照价格排序
		//TODO 到时候可能需要根据模式选用不通类型的价格进行排序
		gaps.sort(Comparator.comparingInt(x -> x.getPrice().getPrice()));

		//构建所有的比价单元, 目前会有多少维度呢?
		LinkedList<MultiTicketSelectUnit> units = new LinkedList<>();
		//存放已经满的比价单元
		LinkedList<MultiTicketSelectUnit> fullUnits = new LinkedList<>();

		//遍历每个价格
		for (GroupAndPrice gap : gaps) {
			Iterator<MultiTicketSelectUnit> iter = units.iterator();
			while (iter.hasNext()) {
				MultiTicketSelectUnit u = iter.next();
				u.add(gap);
				if (u.isOvertake()) {
					iter.remove();
					fullUnits.add(u);
				}
			}
			if (units.isEmpty()) {
				break;
			}
		}

		//此处有两种策略: AGG里大部分是用1, 考虑到结果的数量会比较小, 所以2.的方案可能不差?
		//1. 从结果集里删除非法结果
		//2. 将合法结果构建成结果集

		List<FlightGroup> finalGroups1 = getFinalResult1(gs, gaps, units, fullUnits);
		//List<FlightGroup> finalGroups2 = getFinalResult2(gs,gaps, units,fullUnits);
	}

	private List<FlightGroup> getFinalResult1(List<FlightGroup> gs, List<GroupAndPrice> gaps, LinkedList<MultiTicketSelectUnit> units, LinkedList<MultiTicketSelectUnit> fullUnits) {
		//先将所有结果标记为needRemove
		for (GroupAndPrice gap : gaps) {
			gap.getPrice().setNeedRemove(true);
		}
		for (MultiTicketSelectUnit u : units) {
			for (GroupAndPrice gap : u.getSelectResult()) {
				gap.getPrice().setNeedRemove(false);
			}
		}
		//这些units满了 还要吗?
		for (MultiTicketSelectUnit u : fullUnits) {
//			for (GroupAndPrice gap : u.getSelectResult()) {
//				gap.getPrice().setNeedRemove(false);
//			}
		}
		//遍历gs 删除needRemove的Price和空group
		//内部是用倒叙遍历实现的 效率高一些些些
		gs.removeIf(g -> {
			g.getPrices().removeIf(FlightPrice::isNeedRemove);
			return g.getPrices().isEmpty();
		});
		return gs;
	}

	private List<FlightGroup> getFinalResult2(List<FlightGroup> gs, List<GroupAndPrice> gaps, LinkedList<MultiTicketSelectUnit> units, LinkedList<MultiTicketSelectUnit> fullUnits) {
		Set<Integer> groupIndexSet = new HashSet<>();
		Set<Integer> groupIdSet = new HashSet<>();
		List<FlightGroup> newGs = new ArrayList<>();
		for (MultiTicketSelectUnit u : units) {
			for (GroupAndPrice gap : u.getSelectResult()) {
				if (groupIdSet.add(gap.getGroupId())) {
					continue;
				}
				if (groupIndexSet.add(gap.getGroupId())) {
					gap.getGroup().getPrices().clear();
					newGs.add(gap.getGroup());
				}
				gap.getGroup().getPrices().add(gap.getPrice());
			}
		}

		for (MultiTicketSelectUnit u : fullUnits) {
			for (GroupAndPrice gap : u.getSelectResult()) {
				if (groupIdSet.add(gap.getGroupId())) {
					continue;
				}
				if (groupIndexSet.add(gap.getGroupId())) {
					gap.getGroup().getPrices().clear();
					newGs.add(gap.getGroup());
				}
				gap.getGroup().getPrices().add(gap.getPrice());
			}
		}

		return newGs;
	}
}
