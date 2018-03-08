package com.xzchaoo.learn.jmh.foo;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * @author zcxu
 * @date 2018/1/11
 */
@Fork(1)
@Warmup(iterations = 2)
@Measurement(iterations = 3)
@State(Scope.Benchmark)
public class FooTest {
	private long[] array_long;
	private int[] array_int;
	private short[] array_short;

	@Setup
	public void setup() {
		array_long = new long[64];
		for (int i = 1; i < 64; ++i) {
			array_long[i] = (1 << i) - 1;
		}
		array_int = new int[32];
		for (int i = 1; i < 32; ++i) {
			array_int[i] = (1 << i) - 1;
		}
		array_short = new short[16];
		for (int i = 1; i < 16; ++i) {
			array_short[i] = (short) ((1 << i) - 1);
		}
	}

	@Benchmark
	public void long_1(Blackhole b) {
		long sum = 0;
		for (int i = 0; i < 10000; ++i) {
			int d = i % 63 + 1;
			long result = getLongLimit(d);
			sum += result;
		}
		b.consume(sum);
	}

	@Benchmark
	public void long_2(Blackhole b) {
		long sum = 0;
		for (int i = 0; i < 10000; ++i) {
			int d = i % 63 + 1;
			if (d >= 1 && d <= 63) {
				long result = (1 << d) - 1;
				sum += result;
			}
		}
		b.consume(sum);
	}

	@Benchmark
	public void long_3(Blackhole b) {
		long sum = 0;
		for (int i = 0; i < 10000; ++i) {
			int d = i % 63 + 1;
			if (d >= 1 && d <= 63) {
				long result = array_long[d];
				sum += result;
			}
		}
		b.consume(sum);
	}


	@Benchmark
	public void int_1(Blackhole b) {
		int sum = 0;
		for (int i = 0; i < 10000; ++i) {
			int d = i % 31 + 1;
			int result = getIntegerLimit(d);
			sum += result;
		}
		b.consume(sum);
	}

	@Benchmark
	public void int_2(Blackhole b) {
		int sum = 0;
		for (int i = 0; i < 10000; ++i) {
			int d = i % 31 + 1;
			if (d >= 1 && d <= 31) {
				int result = (1 << d) - 1;
				sum += result;
			}
		}
		b.consume(sum);
	}

	@Benchmark
	public void int_3(Blackhole b) {
		int sum = 0;
		for (int i = 0; i < 10000; ++i) {
			int d = i % 31 + 1;
			if (d >= 1 && d <= 31) {
				int result = array_int[d];
				sum += result;
			}
		}
		b.consume(sum);
	}


	@Benchmark
	public void short_1(Blackhole b) {
		short sum = 0;
		for (int i = 0; i < 10000; ++i) {
			int d = i % 15 + 1;
			short result = getShortLimit(d);
			sum += result;
		}
		b.consume(sum);
	}

	@Benchmark
	public void short_2(Blackhole b) {
		short sum = 0;
		for (int i = 0; i < 10000; ++i) {
			int d = i % 15 + 1;
			if (d >= 1 && d <= 15) {
				short result = (short) ((1 << d) - 1);
				sum += result;
			}
		}
		b.consume(sum);
	}

	@Benchmark
	public void short_3(Blackhole b) {
		short sum = 0;
		for (int i = 0; i < 10000; ++i) {
			int d = i % 15 + 1;
			if (d >= 1 && d <= 15) {
				short result = array_short[d];
				sum += result;
			}
		}
		b.consume(sum);
	}

	private short getShortLimit(int digits) {
		switch (digits) {
			case 1:
				return ConstValue.SHORT_DIGIT_1;
			case 2:
				return ConstValue.SHORT_DIGIT_2;
			case 3:
				return ConstValue.SHORT_DIGIT_3;
			case 4:
				return ConstValue.SHORT_DIGIT_4;
			case 5:
				return ConstValue.SHORT_DIGIT_5;
			case 6:
				return ConstValue.SHORT_DIGIT_6;
			case 7:
				return ConstValue.SHORT_DIGIT_7;
			case 8:
				return ConstValue.SHORT_DIGIT_8;
			case 9:
				return ConstValue.SHORT_DIGIT_9;
			case 10:
				return ConstValue.SHORT_DIGIT_10;
			case 11:
				return ConstValue.SHORT_DIGIT_11;
			case 12:
				return ConstValue.SHORT_DIGIT_12;
			case 13:
				return ConstValue.SHORT_DIGIT_13;
			case 14:
				return ConstValue.SHORT_DIGIT_14;
			case 15:
				return ConstValue.SHORT_DIGIT_15;
			case 16:
				return ConstValue.SHORT_DIGIT_15;
			case 17:
				return ConstValue.SHORT_DIGIT_15;
			case 18:
				return ConstValue.SHORT_DIGIT_15;
			case 19:
				return ConstValue.SHORT_DIGIT_15;
			case 20:
				return ConstValue.SHORT_DIGIT_15;
			case 21:
				return ConstValue.SHORT_DIGIT_15;
			case 22:
				return ConstValue.SHORT_DIGIT_15;
			case 23:
				return ConstValue.SHORT_DIGIT_15;
			case 24:
				return ConstValue.SHORT_DIGIT_15;
			case 25:
				return ConstValue.SHORT_DIGIT_15;
			case 26:
				return ConstValue.SHORT_DIGIT_15;
			case 27:
				return ConstValue.SHORT_DIGIT_15;
			case 28:
				return ConstValue.SHORT_DIGIT_15;
			case 29:
				return ConstValue.SHORT_DIGIT_15;
			case 30:
				return ConstValue.SHORT_DIGIT_15;
			case 31:
				return ConstValue.SHORT_DIGIT_15;
			case 32:
				return ConstValue.SHORT_DIGIT_15;
			case 33:
				return ConstValue.SHORT_DIGIT_15;
			case 34:
				return ConstValue.SHORT_DIGIT_15;
			case 35:
				return ConstValue.SHORT_DIGIT_15;
			case 36:
				return ConstValue.SHORT_DIGIT_15;
			case 37:
				return ConstValue.SHORT_DIGIT_15;
			case 38:
				return ConstValue.SHORT_DIGIT_15;
			case 39:
				return ConstValue.SHORT_DIGIT_15;
			case 40:
				return ConstValue.SHORT_DIGIT_15;
			case 41:
				return ConstValue.SHORT_DIGIT_15;
			case 42:
				return ConstValue.SHORT_DIGIT_15;
			case 43:
				return ConstValue.SHORT_DIGIT_15;
			case 44:
				return ConstValue.SHORT_DIGIT_15;
			case 45:
				return ConstValue.SHORT_DIGIT_15;
			case 46:
				return ConstValue.SHORT_DIGIT_15;
			case 47:
				return ConstValue.SHORT_DIGIT_15;
			case 48:
				return ConstValue.SHORT_DIGIT_15;
			case 49:
				return ConstValue.SHORT_DIGIT_15;
			case 50:
				return ConstValue.SHORT_DIGIT_15;
			case 51:
				return ConstValue.SHORT_DIGIT_15;
			case 52:
				return ConstValue.SHORT_DIGIT_15;
			case 53:
				return ConstValue.SHORT_DIGIT_15;
			case 54:
				return ConstValue.SHORT_DIGIT_15;
			case 55:
				return ConstValue.SHORT_DIGIT_15;
			case 56:
				return ConstValue.SHORT_DIGIT_15;
			case 57:
				return ConstValue.SHORT_DIGIT_15;
			case 58:
				return ConstValue.SHORT_DIGIT_15;
			case 59:
				return ConstValue.SHORT_DIGIT_15;
			case 60:
				return ConstValue.SHORT_DIGIT_15;
			case 61:
				return ConstValue.SHORT_DIGIT_15;
			case 62:
				return ConstValue.SHORT_DIGIT_15;
			case 63:
				return ConstValue.SHORT_DIGIT_15;
			default:
				throw new IllegalArgumentException(String.format("%d digits exceed limit!", digits));
		}
	}

	private int getIntegerLimit(int digits) {
		switch (digits) {
			case 1:
				return ConstValue.INTEGER_DIGIT_1;
			case 2:
				return ConstValue.INTEGER_DIGIT_2;
			case 3:
				return ConstValue.INTEGER_DIGIT_3;
			case 4:
				return ConstValue.INTEGER_DIGIT_4;
			case 5:
				return ConstValue.INTEGER_DIGIT_5;
			case 6:
				return ConstValue.INTEGER_DIGIT_6;
			case 7:
				return ConstValue.INTEGER_DIGIT_7;
			case 8:
				return ConstValue.INTEGER_DIGIT_8;
			case 9:
				return ConstValue.INTEGER_DIGIT_9;
			case 10:
				return ConstValue.INTEGER_DIGIT_10;
			case 11:
				return ConstValue.INTEGER_DIGIT_11;
			case 12:
				return ConstValue.INTEGER_DIGIT_12;
			case 13:
				return ConstValue.INTEGER_DIGIT_13;
			case 14:
				return ConstValue.INTEGER_DIGIT_14;
			case 15:
				return ConstValue.INTEGER_DIGIT_15;
			case 16:
				return ConstValue.INTEGER_DIGIT_16;
			case 17:
				return ConstValue.INTEGER_DIGIT_17;
			case 18:
				return ConstValue.INTEGER_DIGIT_18;
			case 19:
				return ConstValue.INTEGER_DIGIT_19;
			case 20:
				return ConstValue.INTEGER_DIGIT_20;
			case 21:
				return ConstValue.INTEGER_DIGIT_21;
			case 22:
				return ConstValue.INTEGER_DIGIT_22;
			case 23:
				return ConstValue.INTEGER_DIGIT_23;
			case 24:
				return ConstValue.INTEGER_DIGIT_24;
			case 25:
				return ConstValue.INTEGER_DIGIT_25;
			case 26:
				return ConstValue.INTEGER_DIGIT_26;
			case 27:
				return ConstValue.INTEGER_DIGIT_27;
			case 28:
				return ConstValue.INTEGER_DIGIT_28;
			case 29:
				return ConstValue.INTEGER_DIGIT_29;
			case 30:
				return ConstValue.INTEGER_DIGIT_30;
			case 31:
				return ConstValue.INTEGER_DIGIT_31;
			case 32:
				return ConstValue.INTEGER_DIGIT_31;
			case 33:
				return ConstValue.INTEGER_DIGIT_31;
			case 34:
				return ConstValue.INTEGER_DIGIT_31;
			case 35:
				return ConstValue.INTEGER_DIGIT_31;
			case 36:
				return ConstValue.INTEGER_DIGIT_31;
			case 37:
				return ConstValue.INTEGER_DIGIT_31;
			case 38:
				return ConstValue.INTEGER_DIGIT_31;
			case 39:
				return ConstValue.INTEGER_DIGIT_31;
			case 40:
				return ConstValue.INTEGER_DIGIT_31;
			case 41:
				return ConstValue.INTEGER_DIGIT_31;
			case 42:
				return ConstValue.INTEGER_DIGIT_31;
			case 43:
				return ConstValue.INTEGER_DIGIT_31;
			case 44:
				return ConstValue.INTEGER_DIGIT_31;
			case 45:
				return ConstValue.INTEGER_DIGIT_31;
			case 46:
				return ConstValue.INTEGER_DIGIT_31;
			case 47:
				return ConstValue.INTEGER_DIGIT_31;
			case 48:
				return ConstValue.INTEGER_DIGIT_31;
			case 49:
				return ConstValue.INTEGER_DIGIT_31;
			case 50:
				return ConstValue.INTEGER_DIGIT_31;
			case 51:
				return ConstValue.INTEGER_DIGIT_31;
			case 52:
				return ConstValue.INTEGER_DIGIT_31;
			case 53:
				return ConstValue.INTEGER_DIGIT_31;
			case 54:
				return ConstValue.INTEGER_DIGIT_31;
			case 55:
				return ConstValue.INTEGER_DIGIT_31;
			case 56:
				return ConstValue.INTEGER_DIGIT_31;
			case 57:
				return ConstValue.INTEGER_DIGIT_31;
			case 58:
				return ConstValue.INTEGER_DIGIT_31;
			case 59:
				return ConstValue.INTEGER_DIGIT_31;
			case 60:
				return ConstValue.INTEGER_DIGIT_31;
			case 61:
				return ConstValue.INTEGER_DIGIT_31;
			case 62:
				return ConstValue.INTEGER_DIGIT_31;
			case 63:
				return ConstValue.INTEGER_DIGIT_31;
			default:
				throw new IllegalArgumentException(String.format("%d digits exceed limit!", digits));
		}
	}

	private long getLongLimit(int digits) {
		switch (digits) {
			case 1:
				return ConstValue.LONG_DIGIT_1;
			case 2:
				return ConstValue.LONG_DIGIT_2;
			case 3:
				return ConstValue.LONG_DIGIT_3;
			case 4:
				return ConstValue.LONG_DIGIT_4;
			case 5:
				return ConstValue.LONG_DIGIT_5;
			case 6:
				return ConstValue.LONG_DIGIT_6;
			case 7:
				return ConstValue.LONG_DIGIT_7;
			case 8:
				return ConstValue.LONG_DIGIT_8;
			case 9:
				return ConstValue.LONG_DIGIT_9;
			case 10:
				return ConstValue.LONG_DIGIT_10;
			case 11:
				return ConstValue.LONG_DIGIT_11;
			case 12:
				return ConstValue.LONG_DIGIT_12;
			case 13:
				return ConstValue.LONG_DIGIT_13;
			case 14:
				return ConstValue.LONG_DIGIT_14;
			case 15:
				return ConstValue.LONG_DIGIT_15;
			case 16:
				return ConstValue.LONG_DIGIT_16;
			case 17:
				return ConstValue.LONG_DIGIT_17;
			case 18:
				return ConstValue.LONG_DIGIT_18;
			case 19:
				return ConstValue.LONG_DIGIT_19;
			case 20:
				return ConstValue.LONG_DIGIT_20;
			case 21:
				return ConstValue.LONG_DIGIT_21;
			case 22:
				return ConstValue.LONG_DIGIT_22;
			case 23:
				return ConstValue.LONG_DIGIT_23;
			case 24:
				return ConstValue.LONG_DIGIT_24;
			case 25:
				return ConstValue.LONG_DIGIT_25;
			case 26:
				return ConstValue.LONG_DIGIT_26;
			case 27:
				return ConstValue.LONG_DIGIT_27;
			case 28:
				return ConstValue.LONG_DIGIT_28;
			case 29:
				return ConstValue.LONG_DIGIT_29;
			case 30:
				return ConstValue.LONG_DIGIT_30;
			case 31:
				return ConstValue.LONG_DIGIT_31;
			case 32:
				return ConstValue.LONG_DIGIT_32;
			case 33:
				return ConstValue.LONG_DIGIT_33;
			case 34:
				return ConstValue.LONG_DIGIT_34;
			case 35:
				return ConstValue.LONG_DIGIT_35;
			case 36:
				return ConstValue.LONG_DIGIT_36;
			case 37:
				return ConstValue.LONG_DIGIT_37;
			case 38:
				return ConstValue.LONG_DIGIT_38;
			case 39:
				return ConstValue.LONG_DIGIT_39;
			case 40:
				return ConstValue.LONG_DIGIT_40;
			case 41:
				return ConstValue.LONG_DIGIT_41;
			case 42:
				return ConstValue.LONG_DIGIT_42;
			case 43:
				return ConstValue.LONG_DIGIT_43;
			case 44:
				return ConstValue.LONG_DIGIT_44;
			case 45:
				return ConstValue.LONG_DIGIT_45;
			case 46:
				return ConstValue.LONG_DIGIT_46;
			case 47:
				return ConstValue.LONG_DIGIT_47;
			case 48:
				return ConstValue.LONG_DIGIT_48;
			case 49:
				return ConstValue.LONG_DIGIT_49;
			case 50:
				return ConstValue.LONG_DIGIT_50;
			case 51:
				return ConstValue.LONG_DIGIT_51;
			case 52:
				return ConstValue.LONG_DIGIT_52;
			case 53:
				return ConstValue.LONG_DIGIT_53;
			case 54:
				return ConstValue.LONG_DIGIT_54;
			case 55:
				return ConstValue.LONG_DIGIT_55;
			case 56:
				return ConstValue.LONG_DIGIT_56;
			case 57:
				return ConstValue.LONG_DIGIT_57;
			case 58:
				return ConstValue.LONG_DIGIT_58;
			case 59:
				return ConstValue.LONG_DIGIT_59;
			case 60:
				return ConstValue.LONG_DIGIT_60;
			case 61:
				return ConstValue.LONG_DIGIT_61;
			case 62:
				return ConstValue.LONG_DIGIT_62;
			case 63:
				return ConstValue.LONG_DIGIT_63;
			default:
				throw new IllegalArgumentException(String.format("%d digits exceed limit!", digits));
		}
	}
}
