package com.xzchaoo.learn.eclipse.collections;

import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Sets;
import org.junit.Test;

/**
 * created by xzchaoo at 2017/11/25
 *
 * @author xzchaoo
 */
public class SetTest {
	@Test
	public void test() {
		ImmutableSet<Integer> s = Sets.immutable.of(1, 2, 3);
		s.castToSet();
	}
}
