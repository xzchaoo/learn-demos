package com.xzchaoo.learn.jmh.example;

import com.xzchaoo.learn.jmh.UserCopyMapper;
import com.xzchaoo.learn.jmh.UserForJson;

import org.apache.commons.beanutils.BeanUtilsBean2;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;

/**
 * created by xzchaoo at 2017/12/1
 *
 * @author xzchaoo
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
@Fork(1)
public class ObjectCopyTest {

	@State(Scope.Benchmark)
	public static class CloneState {
		public UserForJson user;

		@Setup
		public void setup() {
			user = new UserForJson();
			user.setId(1);
			user.setLong1(1);
			user.setUsername("i am username");
			user.setFloat1(12.456f);
			user.setDouble1(121221.234);
			user.setList1(Arrays.asList("111", "222", "333333"));
			user.setDate1(new Date());
		}
	}

	//重载clone方法 利用浅复制
	@Benchmark
	public void clone2(CloneState state, Blackhole b) {
		b.consume(state.user.clone());
	}

	//手动赋值
	@Benchmark
	public void manual(CloneState state, Blackhole b) {
		UserForJson u1 = state.user;
		UserForJson u2 = new UserForJson();
		u2.setId(u1.getId());
		u2.setLong1(u1.getLong1());
		u2.setUsername(u1.getUsername());
		u2.setFloat1(u1.getFloat1());
		u2.setDouble1(u1.getDouble1());
		u2.setList1(u1.getList1());
		u2.setDate1(u1.getDate1());
		b.consume(u2);
	}

	//使用beanutils, 基于反射
	@Benchmark
	public void beanUtils(CloneState state, Blackhole b) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
		Object u = BeanUtilsBean2.getInstance().cloneBean(state.user);
		b.consume(u);
	}

	@Benchmark
	public void mapstruct(CloneState state, Blackhole b) {
		UserCopyMapper m = UserCopyMapper.INSTANCE;
		UserForJson u = m.shadowCopy(state.user);
		b.consume(u);
	}
}
