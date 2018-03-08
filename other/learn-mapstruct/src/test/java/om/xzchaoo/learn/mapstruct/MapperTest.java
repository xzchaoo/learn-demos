package om.xzchaoo.learn.mapstruct;

import com.xzchaoo.learn.mapstruct.User;
import com.xzchaoo.learn.mapstruct.UserEntity;
import com.xzchaoo.learn.mapstruct.UserMapper;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * created by xzchaoo at 2017/11/22
 *
 * @author xzchaoo
 */
public class MapperTest {
	@Test
	public void test() {
		UserMapper um = UserMapper.INSTANCE;
		UserEntity e = null;
		User u = um.convertUserEntityToUser(e);
		assertNull(u);

		e = new UserEntity();
		e.setId(1L);
		e.setUsername("xzc");
		e.setPassword("aaa");
		e.setChannels("a/b/c/");
		e.setRegisterAt(new Date());
		u = um.convertUserEntityToUser(e);
		assertNotNull(u);
		assertEquals(1, u.getId());
		assertEquals("xzc", u.getUsername());
		System.out.println(u.getRegisterAt());
		System.out.println(u.getType());
	}
}
