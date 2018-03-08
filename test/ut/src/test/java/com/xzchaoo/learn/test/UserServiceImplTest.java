package com.xzchaoo.learn.test;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * mock系列 @Mock when thenReturn answer
 */
public class UserServiceImplTest extends TestBase {
	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private UserDao userDao;

	@Test
	public void test() {
		//对于满足这个条件的参数 返回这个值
		//匹配参数 anyString() anyInt() eq("")
		//不可以和字面值常量一起用

		when(userDao.findUserByUserId(intThat(new ArgumentMatcher<Integer>() {
			@Override
			public boolean matches(Integer argument) {
				return argument % 2 == 1;
			}
		}))).then(i -> {
			User user = new User();
			user.setId(1);
			user.setName("xzc");
			return user;
		});
		String username = userService.getUsernameByUserId(1);
		assertEquals("xzc", username);
		verify(userDao, times(1)).findUserByUserId(1);
		verify(userDao, atLeast(1)).findUserByUserId(1);
		verify(userDao, atMost(1)).findUserByUserId(1);
		//verify(userDao, never()).findUserByUserId(1);
		doNothing().when(userDao).f1(anyInt());

		//如果顺序很重要 就用下面的方式来代替原有的验证
//		InOrder inOrder = inOrder(userDao);
//		inOrder.verify()
	}
}
