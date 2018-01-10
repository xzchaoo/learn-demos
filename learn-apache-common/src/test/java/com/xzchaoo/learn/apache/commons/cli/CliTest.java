package com.xzchaoo.learn.apache.commons.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author xzchaoo
 * @date 2018/1/7
 */
public class CliTest {
	@Test
	public void test() throws ParseException {
		Options os = new Options()
			.addOption("v", "verbose", false, "verbose")
			.addOption(
				Option.builder("n")
					.longOpt("name")
					.desc("名字")
					.required()
					.numberOfArgs(1)
					.build()
			)
			.addOption(
				Option.builder()
					.longOpt("qq")
					.desc("QQ号")
					.hasArgs()
					.valueSeparator(',')
					.build()
			)
			.addOption(
				Option.builder("D")
					.desc("添加系统属性")
					.argName("key=value")
					.hasArgs()
					.valueSeparator('=')
					.build()
			)
			.addOptionGroup(
				new OptionGroup()
					.addOption(Option.builder("f1").desc("d1").hasArg().optionalArg(false).build())
					.addOption(Option.builder("f2").desc("d2").hasArg().build())
					.addOption(Option.builder("f3").desc("d3").hasArg().build())
			);
		System.out.println(os.toString());

		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("my-app", os);


		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(os, new String[]{"--qq=1,2,3", "-v", "-Dkey1=value1", "-Dkey2=value2", "-n", "xzc", "true"});
		assertTrue(cmd.hasOption("verbose"));
		assertEquals(false, Boolean.parseBoolean(cmd.getOptionValue("verbose", "false")));
		assertTrue(cmd.hasOption("name"));
		assertTrue(cmd.hasOption("n"));
		assertEquals("xzc", cmd.getOptionValue("name"));
		System.out.println(cmd.getOptionValues("qq").length);
		System.out.println(Arrays.asList(cmd.getOptionValues("D")));
		cmd.getParsedOptionValue("a");
	}
}
