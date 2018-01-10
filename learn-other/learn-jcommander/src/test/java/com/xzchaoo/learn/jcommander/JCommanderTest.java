package com.xzchaoo.learn.jcommander;

import com.beust.jcommander.JCommander;

import org.junit.Test;

/**
 * @author xzchaoo
 * @date 2018/1/7
 */
public class JCommanderTest {
	@Test
	public void test1() {
		Args args = new Args();
		CommandCommit cc = new CommandCommit();
		JCommander jc = JCommander.newBuilder()
			.addObject(args)
			.addCommand(cc)
			.acceptUnknownOptions(true)
			.build();
		jc.usage();
		jc.parse(new String[]{"-a", "1", "-v", "a", "b", "c", "-debug=true", "-host=a", "-host", "B", "-password", "commit", "--", "a", "b", "c"});

		System.out.println(jc.getParsedCommand());

		//assertEquals(Arrays.asList("a", "b", "c"), args.getParameters());
//		assertTrue(args.isVerbose());
//		assertTrue(args.isDebug());
		System.out.println(cc);
		System.out.println(args);
	}
}
